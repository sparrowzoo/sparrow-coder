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

package com.sparrow.coder.domain.bo;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.protocol.BO;
import com.sparrow.utility.StringUtility;
import com.sparrow.coder.domain.service.ArchitectureGenerator;
import com.sparrow.coder.domain.service.registry.ArchitectureRegistry;
import com.sparrow.coder.enums.ArchitectureCategory;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ProjectArchsBO implements BO {
    public ProjectArchsBO(String configs) {
        this.archs = JsonFactory.getProvider().parse(configs, Map.class);
        if (this.archs == null || this.archs.isEmpty()) {
            this.archs = new HashMap<>();
            for (ArchitectureCategory category : ArchitectureCategory.values()) {
                this.archs.put(category.name(), category.getDefaultArch());
            }
        }
    }

    public ArchitectureGenerator getArch(ArchitectureCategory category) {
        String arch = this.archs.get(category.name());
        if (StringUtility.isNullOrEmpty(arch)) {
            arch = category.getDefaultArch();
        }
        return ArchitectureRegistry.getInstance().getGenerator(category, arch);
    }

    public ArchitectureGenerator getArch(String category) {
        return this.getArch(ArchitectureCategory.valueOf(category));
    }

    private Map<String, String> archs;
}
