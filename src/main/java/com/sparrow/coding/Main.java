package com.sparrow.coding;

/**
 * Created by harry on 18/11/16.
 */
public class Main {
    private static void useage() {
        System.out.printf("usage:\n");
        System.out.printf("sparrow-coder.sh [desc object] [source object] [source object]\n");
        System.out.printf("desc object [dao assemble service controller]\n");
        System.out.printf("source object [po dto vo]\n");
    }

    public static void main(String[] args) throws Exception {
        args = new String[]{"-c", "com.sparrow.user.po.User"};
        if (args.length == 0 || "--help".equals(args[0])) {
            useage();
            System.exit(0);
        }

        if (args.length < 2) {
            useage();
            System.exit(0);
        }

        Class po = Class.forName(args[1]);

        CodeGenerator codeGenerator = new CodeGenerator();
        if ("-d".equalsIgnoreCase(args[0]) || "-dao".equalsIgnoreCase(args[0])) {

            codeGenerator.dao(po);
            System.exit(0);
        }

        if ("-di".equalsIgnoreCase(args[0]) || "-daoimpl".equalsIgnoreCase(args[0])) {
            codeGenerator.daoImpl(po);
            System.exit(0);
        }

        if ("-s".contentEquals(args[0]) || "-service".equalsIgnoreCase(args[0])) {
            codeGenerator.service(po);
            System.exit(0);
        }


        if ("-si".contentEquals(args[0]) || "-serviceimpl".equalsIgnoreCase(args[0])) {
            codeGenerator.serviceImpl(po);
            System.exit(0);
        }

        if ("-c".contentEquals(args[0]) || "-controller".equalsIgnoreCase(args[0])) {
            codeGenerator.controller(po);
            System.exit(0);
        }

        if ("-t".equals(args[0]) || "-table_tempalte".equals(args[0])) {
            codeGenerator.writeTableConfig(po);
            System.exit(0);
        }
    }
}
