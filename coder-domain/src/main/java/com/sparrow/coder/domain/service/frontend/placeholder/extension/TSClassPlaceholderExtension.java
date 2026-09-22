/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.coder.domain.service.frontend.placeholder.extension;

import com.sparrow.orm.Field;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.enums.PlaceholderKey;
import com.sparrow.coder.utils.JavaTsTypeConverter;
import jakarta.inject.Named;

import java.util.Map;

@Named
public class TSClassPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        Map<String, String> placeholder = tableContext.getPlaceHolder();
        placeholder.put(PlaceholderKey.$frontend_class.name(), this.generateClass(tableContext));
    }

    private String generateClass(TableContext tableContext) {
        Map<String, Field> fields = tableContext.getEntityManager().getPropertyFieldMap();
        StringBuilder fieldBuild = new StringBuilder();
        for (Field field : fields.values()) {
            Class<?> fieldClazz = field.getType();
            String property = String.format("%s:%s; \n", field.getPropertyName(), JavaTsTypeConverter.toTsType(fieldClazz));
            fieldBuild.append(property);
        }
        String className = tableContext.getTableConfig().getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);

        return String.format("export interface %1$s extends BasicData<%1$s> \n{\n %2$s\n}", className, fieldBuild);
    }
}
