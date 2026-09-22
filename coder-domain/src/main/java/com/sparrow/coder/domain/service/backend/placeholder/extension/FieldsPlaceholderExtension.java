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

package com.sparrow.coder.domain.service.backend.placeholder.extension;

import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.enums.PlaceholderKey;
import jakarta.inject.Named;

import java.util.Map;

@Named
public class FieldsPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        Map<String, String> placeHolder = tableContext.getPlaceHolder();
        EntityManager entityManager = tableContext.getEntityManager();
        Map<String, Field> fields = entityManager.getPropertyFieldMap();
        boolean isAssignableDisplayText = entityManager.isAssignableFromDisplayText();
        String getSetPrefix = isAssignableDisplayText ? ",DisplayTextAccessor{" : "{";
        StringBuilder fieldBuild = new StringBuilder(getSetPrefix);
        StringBuilder fieldDtoBuild = new StringBuilder(getSetPrefix);
        StringBuilder paramFieldBuild = new StringBuilder();

        for (Field field : fields.values()) {
            Class<?> fieldClazz = field.getType();
            String property = String.format("private %s %s; \n", fieldClazz.getSimpleName(), field.getPropertyName());
            fieldBuild.append(property);
            if (field.getType().equals(StatusRecord.class)) {
                property = String.format("private Integer %s; \n", field.getPropertyName());
            }
            fieldDtoBuild.append(property);
            if (entityManager.getPoPropertyNames() != null && !entityManager.getPoPropertyNames().contains(field.getPropertyName())) {
                paramFieldBuild.append(property);
            }
        }
        //如果实现了DisplayText 接口，则生成KVS的接口，提供给外表关联使用
        if (isAssignableDisplayText) {
            fieldBuild.append("private String displayText; \n");
            fieldDtoBuild.append("private String displayText; \n");

        }
        fieldBuild.append("}");
        fieldDtoBuild.append("}");
        if (entityManager.getStatus() != null) {
            placeHolder.put(PlaceholderKey.$status_field.name(), entityManager.getStatus().getPropertyName());
        }
        placeHolder.put(PlaceholderKey.$get_sets_dto.name(), fieldDtoBuild.toString());
        placeHolder.put(PlaceholderKey.$get_sets.name(), fieldBuild.toString());
        placeHolder.put(PlaceholderKey.$get_sets_params.name(), paramFieldBuild.toString());
    }
}
