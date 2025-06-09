package com.sparrow.coding;

public class CodeGeneratorDemo {
    public static void main(String[] args) throws Exception {
        //args = "--example".split(" ");
        args = "-b com.sparrow.coding.protocol.TableDef".split(" ");
        //args = "-pq com.sparrow.example.po.SparrowExample -config=/Users/zhanglizhi/workspace/tedu/tarena-tp-basic/basic-po/bin/config.properties".split(" ");
        JavaMain.main(args);
    }
}
