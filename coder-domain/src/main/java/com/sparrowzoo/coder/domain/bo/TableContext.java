package com.sparrowzoo.coder.domain.bo;

import com.sparrow.orm.EntityManager;
import com.sparrowzoo.coder.po.TableConfig;
import lombok.Data;

import java.util.Map;

@Data
public class TableContext {
    private Map<String, String> placeHolder;
    private TableConfigBO tableConfig;
    private EntityManager entityManager;
    public String getPoPackage(){
        String className=tableConfig.getClassName();
        return className.substring(0,className.lastIndexOf("."));
    }
}
