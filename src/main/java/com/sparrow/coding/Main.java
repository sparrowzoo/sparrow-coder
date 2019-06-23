package com.sparrow.coding;

import com.sparrow.orm.AbstractEntityManagerAdapter;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.utility.FileUtility;

/**
 * Created by harry on 18/11/16.
 */
public class Main {
    private static void useage() {
        System.out.printf("usage:\n");
        System.out.printf("sparrow-coder.sh [OPTION] [args]\n");
        System.out.printf("OPTION:\n");
        System.out.printf("DAO:             [-d |-dao]          args=po\n");
        System.out.printf("daoImpl:         [-di|-daoImpl]      args=po\n");
        System.out.printf("service:         [-s |-service]      args=po\n");
        System.out.printf("serviceImpl:     [-si|-serviceImpl]  args=po\n");
        System.out.printf("controller:      [-c |-controller]   args=po\n");
        System.out.printf("table template:  [-t |-template]     args=po\n");
        System.out.printf("create ddl:      [-ct|-createDDL]    args=po\n");
        System.out.printf("assemble:        [-a |-assemble]     args=pojo,pojo\n");
    }

    public static void main(String[] args) throws Exception {
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

        GenerateAssembleCode generateAssembleCode=new GenerateAssembleCode();
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

        if ("-t".equals(args[0]) || "-template".equals(args[0])) {
            codeGenerator.generateTableTemplate(po);
            System.exit(0);
        }

        if ("-ct".equals(args[0]) || "-createDDL".equals(args[0])) {
            codeGenerator.generaCreateDDL(po);
            System.exit(0);
        }

        if ("-a".equals(args[0]) || "-assemble".equals(args[0])) {
            Class source=Class.forName(args[1]);
            Class dest=Class.forName(args[2]);
            StringBuilder code= generateAssembleCode.generate(dest,source);
            System.err.println(code);
            System.exit(0);
        }
    }
}
