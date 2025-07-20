package com.sparrowzoo.coder.protocol.param;

import lombok.Data;

@Data
public class SourceCodeParam {
    private Long projectId;
    private String fullClassName;
    private String sourceCode;
}
