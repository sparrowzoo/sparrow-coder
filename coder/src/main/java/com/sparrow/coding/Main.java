package com.sparrow.coding;

import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;

/**
 * Created by harry on 18/11/16.
 */
public class Main {
    private static void useage() {
        System.out.println("usage:");
        System.out.println("sparrow-coder.sh [OPTION] [args]");
        System.out.println("OPTION:");
        System.out.println("bo:              [-b |-bo]           args=po");
        System.out.println("param:           [-p |-param]        args=po");
        System.out.println("q:               [-q |-query]        args=po");
        System.out.println("vo:              [-v |-vo]           args=po");
        System.out.println("cv:              [-cv|-converter]    args=po");

        System.out.println("dao:             [-d |-dao]          args=po");
        System.out.println("daoImpl:         [-di|-daoImpl]      args=po");
        System.out.println("repository:      [-r |-repository]   args=po");
        System.out.println("repositoryImpl:  [-ri|-repositoryImpl]args=po");
        System.out.println("service:         [-s |-service]      args=po");
        System.out.println("controller:      [-c |-controller]   args=po");
        System.out.println("table template:  [-t |-template]     args=po");
        System.out.println("create ddl:      [-ct|-createDDL]    args=po");
        System.out.println("create ddl-n:    [-ctn|-createDDL-n] args=src-table-name,n -c(create into database)");
        System.out.println("assemble:        [-a |-assemble]     args=pojo,pojo");
    }

    public static void main(String[] args) throws Exception {
//       args = "-c com.sparrow.coding.validating.po.Forum".split(" ");
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
            container.init(new ContainerBuilder());
            if (args.length == 3) {
                codeGenerator.generaCreateNDDL(args[1], Integer.valueOf(args[2]), false);
            } else if (args.length == 4 && args[3].equalsIgnoreCase("-c")) {
                codeGenerator.generaCreateNDDL(args[1], Integer.valueOf(args[2]), true);
            }
            System.exit(0);
        }

        Class po = Class.forName(args[1]);
        GenerateAssembleCode generateAssembleCode = new GenerateAssembleCode();
        if ("-b".equalsIgnoreCase(args[0]) || "-bo".equalsIgnoreCase(args[0])) {
            codeGenerator.bo(po);
            System.exit(0);
        }

        if ("-p".equalsIgnoreCase(args[0]) || "-param".equalsIgnoreCase(args[0])) {
            codeGenerator.param(po);
            System.exit(0);
        }

        if ("-q".equalsIgnoreCase(args[0]) || "-query".equalsIgnoreCase(args[0])) {
            codeGenerator.query(po);
            System.exit(0);
        }

        if ("-a".equals(args[0]) || "-assemble".equalsIgnoreCase(args[0])) {
            codeGenerator.assemble(po);
            System.exit(0);
        }

        if ("-v".equalsIgnoreCase(args[0]) || "-vo".equalsIgnoreCase(args[0])) {
            codeGenerator.vo(po);
            System.exit(0);
        }

        if ("-cv".equalsIgnoreCase(args[0]) || "-converter".equalsIgnoreCase(args[0])) {
            codeGenerator.converter(po);
            System.exit(0);
        }

        if ("-d".equalsIgnoreCase(args[0]) || "-dao".equalsIgnoreCase(args[0])) {
            codeGenerator.dao(po);
            System.exit(0);
        }

        if ("-r".equalsIgnoreCase(args[0]) || "-repository".equalsIgnoreCase(args[0])) {
            codeGenerator.repository(po);
            System.exit(0);
        }

        if ("-di".equalsIgnoreCase(args[0]) || "-daoimpl".equalsIgnoreCase(args[0])) {
            codeGenerator.daoImpl(po);
            System.exit(0);
        }

        if ("-ri".equalsIgnoreCase(args[0]) || "-repositoryImpl".equalsIgnoreCase(args[0])) {
            codeGenerator.repositoryImpl(po);
            System.exit(0);
        }

        if ("-s".contentEquals(args[0]) || "-service".equalsIgnoreCase(args[0])) {
            codeGenerator.service(po);
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

    }
}
