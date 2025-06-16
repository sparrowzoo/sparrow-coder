package com.sparrowzoo.coder.bo;

import com.sparrowzoo.coder.po.TableConfig;
import lombok.Data;

import java.util.Map;

@Data
public class TableContext {
    private Map<String, String> placeHolder;
    private TableConfig tableConfig;
    public String getPoPackage(){
        String className=tableConfig.getClassName();
        return className.substring(0,className.lastIndexOf("."));
    }
}
