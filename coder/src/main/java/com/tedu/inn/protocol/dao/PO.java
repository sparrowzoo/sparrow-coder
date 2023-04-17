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


package com.tedu.inn.protocol.dao;

import javax.persistence.Column;
public class PO {
    @Column(name = "create_user_name", columnDefinition = "varchar(64)  DEFAULT '' COMMENT '创建人'", nullable = false)
    private String createUserName;

    @Column(name = "create_user_id", columnDefinition = "int(11) unsigned  DEFAULT 0 COMMENT '创建人ID'", nullable = false)
    private Long createUserId;
    @Column(name = "modified_user_id", columnDefinition = "int(11) unsigned  DEFAULT 0 COMMENT '更新人ID'", nullable = false)
    private Long modifiedUserId;
    @Column(name = "modified_user_name", columnDefinition = "varchar(64)  DEFAULT '' COMMENT '更新人'", nullable = false)
    private String modifiedUserName;
    @Column(name = "gmt_modified", columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '更新时间'", nullable = false)
    private Long gmtModified;
    @Column(name = "gmt_create", columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '创建时间'", nullable = false, updatable = false)
    private Long gmtCreate;

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getModifiedUserId() {
        return modifiedUserId;
    }

    public void setModifiedUserId(Long modifiedUserId) {
        this.modifiedUserId = modifiedUserId;
    }

    public String getModifiedUserName() {
        return modifiedUserName;
    }

    public void setModifiedUserName(String modifiedUserName) {
        this.modifiedUserName = modifiedUserName;
    }

    public Long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}