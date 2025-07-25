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
package com.sparrowzoo.coder.protocol.dto;

import com.sparrow.protocol.DTO;
import lombok.Data;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.protocol.DisplayTextAccessor;


@Data
public class ProjectConfigDTO implements DTO
    ,DisplayTextAccessor{private Long id; 
private String name; 
private String frontendName; 
private String chineseName; 
private Boolean i18n; 
private String description; 
private String modulePrefix; 
private String architectures; 
private String config; 
private Boolean wrapWithParent; 
private String scaffold; 
private String createUserName; 
private Long createUserId; 
private Long modifiedUserId; 
private String modifiedUserName; 
private Long gmtCreate; 
private Long gmtModified; 
private Boolean deleted; 
private StatusRecord status; 
private String displayText; 
}