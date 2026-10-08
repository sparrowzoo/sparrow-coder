package com.sparrow.coder.domain.service.frontend.generator.column;

import lombok.Data;

@Data
public class ColumnConfig {
    private String id;
    private String accessorKey;
    private String header;
    private String cell;
    private boolean enableHiding;
}
