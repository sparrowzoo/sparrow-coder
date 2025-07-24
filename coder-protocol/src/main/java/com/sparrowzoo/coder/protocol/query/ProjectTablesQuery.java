package com.sparrowzoo.coder.protocol.query;

import lombok.Data;

@Data
public class ProjectTablesQuery {
    private Long projectId;
    private String[] tableNames;
}
