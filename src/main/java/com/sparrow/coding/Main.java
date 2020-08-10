package com.sparrow.coding;

import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;

/**
 * Created by harry on 18/11/16.
 */
public class Main {
    private static void useage() {
        System.out.println("usage:");
        System.out.println("sparrow-coder.sh [OPTION] [args]");
        System.out.println("OPTION:");
        System.out.println("dao:             [-d |-dao]          args=po");
        System.out.println("daoImpl:         [-di|-daoImpl]      args=po");
        System.out.println("service:         [-s |-service]      args=po");
        System.out.println("serviceImpl:     [-si|-serviceImpl]  args=po");
        System.out.println("controller:      [-c |-controller]   args=po");
        System.out.println("table template:  [-t |-template]     args=po");
        System.out.println("create ddl:      [-ct|-createDDL]    args=po");
        System.out.println("create ddl-n:    [-ctn|-createDDL-n] args=src-table-name,n -c");
        System.out.println("assemble:        [-a |-assemble]     args=pojo,pojo");
    }

    public static void main(String[] args) throws Exception {
//        args = "-ctn event 2 -c".split(" ");
        if (args.length == 0 || "--help".equals(args[0])) {
            useage();
            System.exit(0);
        }

        if (args.length < 2) {
            useage();
            System.exit(0);
        }

        CodeGenerator codeGenerator = new CodeGenerator();

        if ("-ctn".equals(args[0]) || "-createDDL-n".equals(args[0])) {
            Container container = ApplicationContext.getContainer();
            container.init();
            if (args.length == 3) {
                codeGenerator.generaCreateNDDL(args[1], Integer.valueOf(args[2]), false);
            } else if (args.length == 4 && args[3].equalsIgnoreCase("-c")) {
                codeGenerator.generaCreateNDDL(args[1], Integer.valueOf(args[2]), true);
            }
            System.exit(0);
        }

        Class po = Class.forName(args[1]);


        GenerateAssembleCode generateAssembleCode = new GenerateAssembleCode();
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

        if ("-t".equals(args[0]) || "-template".equalsIgnoreCase(args[0])) {
            codeGenerator.generateTableTemplate(po);
            System.exit(0);
        }

        if ("-ct".equals(args[0]) || "-createDDL".equalsIgnoreCase(args[0])) {
            codeGenerator.generaCreateDDL(po);
            System.exit(0);
        }


        if ("-a".equals(args[0]) || "-assemble".equalsIgnoreCase(args[0])) {
            Class source = Class.forName(args[1]);
            Class dest = Class.forName(args[2]);
            StringBuilder code = generateAssembleCode.generate(dest, source);
            System.err.println(code);
            System.exit(0);
        }
    }
}
