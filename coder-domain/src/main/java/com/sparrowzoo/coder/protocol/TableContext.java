package com.sparrow.coding.protocol;

import com.sparrow.coding.po.TableConfig;
import lombok.Data;

import java.util.Map;

@Data
public class TableContext {
    private String poPackage;
    private Map<String, String> placeHolder;
    private TableConfig tableConfig;
}
