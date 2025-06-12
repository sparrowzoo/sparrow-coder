package com.sparrow.coder;

public class CodeGeneratorDemo {
    public static void main(String[] args) throws Exception {
        args = "-b com.sparrow.coding.protocol.TableDef".split(" ");
        JavaMain.main(args);
    }
}
