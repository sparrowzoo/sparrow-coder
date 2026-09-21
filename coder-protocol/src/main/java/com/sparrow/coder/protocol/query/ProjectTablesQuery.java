package com.sparrow.coder.protocol.query;

import lombok.Data;

@Data
public class ProjectTablesQuery {
    private Long projectId;
    private String[] tableNames;
}
