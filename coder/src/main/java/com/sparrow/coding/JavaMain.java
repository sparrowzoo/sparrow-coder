package com.sparrow.coding;

import com.sparrow.coding.java.CodeGenerator;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaMain {
    private static void usage() {
        System.out.println("使用方法【usage】:");
        System.out.println("sparrow-java-coder.sh [OPTION] [args]");
        System.out.println("OPTION:");
        System.out.println("生成示例:             [--example]");
        System.out.println("帮助:                [--help]");
        System.out.println("生成BO:              [-b |-bo]           args=po");
        System.out.println("生成Param:           [-p |-param]        args=po");
        System.out.println("生成Query:           [-q |-query]        args=po");
        System.out.println("生成VO:              [-v |-vo]           args=po");
        System.out.println("生成dataConverter:   [-cv|-converter]    args=po");
        System.out.println("生成Pager Query:     [-pq|-pagerQuery]   args=po");
        System.out.println("生成Count Query:     [-cq|-countQuery]   args=po");
        System.out.println("生成BatchOperateParam:[-bop|-BatchOperateParam] args=po");
        System.out.println("生成Dao 接口:         [-d |-dao]          args=po");
        System.out.println("生成dao 实现:         [-di|-daoImpl]      args=po");
        System.out.println("生成仓储层接口:        [-r |-repository]   args=po");
        System.out.println("生成仓储层实现:        [-ri|-repositoryImpl]args=po");
        System.out.println("生成服务层:           [-s |-service]      args=po");
        System.out.println("生成控制层:           [-c |-controller]   args=po");
        System.out.println("生成组装类:           [-a |-assemble]     args=pojo");

        System.out.println("生成建表语句 ddl:      [-ct|-createDDL]    args=po");
        System.out.println("生成分表创建语句 ddl-n: [-ctn|-createDDL-n] args=src-table-name,n -c(create into database)");
    }

    public static void innerMain(String[] args) throws IOException, ClassNotFoundException {
        String sparrowConfig = "default";
        String configPath = args[args.length - 1];
        if (configPath.startsWith("-config=")) {
            sparrowConfig = configPath.substring("-config=".length());
        }
        CodeGenerator codeGenerator = new CodeGenerator(sparrowConfig);
        if ("-ctn".equals(args[0]) || "-createDDL-n".equals(args[0])) {
            if (args.length == 3) {
                codeGenerator.generaCreateNDDL(args[1], Integer.valueOf(args[2]), false);
            } else if (args.length == 4 && args[3].equalsIgnoreCase("-c")) {
                codeGenerator.generaCreateNDDL(args[1], Integer.valueOf(args[2]), true);
            }
            return;
        }

        Class<?> po = Class.forName(args[1]);
        if ("-b".equalsIgnoreCase(args[0]) || "-bo".equalsIgnoreCase(args[0])) {
            codeGenerator.bo(po);
            return;
        }

        if ("-p".equalsIgnoreCase(args[0]) || "-param".equalsIgnoreCase(args[0])) {
            codeGenerator.param(po);
            return;
        }

        if ("-q".equalsIgnoreCase(args[0]) || "-query".equalsIgnoreCase(args[0])) {
            codeGenerator.query(po);
            return;
        }

        if ("-a".equals(args[0]) || "-assemble".equalsIgnoreCase(args[0])) {
            codeGenerator.assemble(po);
            return;
        }

        if ("-v".equalsIgnoreCase(args[0]) || "-vo".equalsIgnoreCase(args[0])) {
            codeGenerator.vo(po);
            return;
        }

        if ("-cv".equalsIgnoreCase(args[0]) || "-converter".equalsIgnoreCase(args[0])) {
            codeGenerator.converter(po);
            return;
        }

        if ("-pq".equalsIgnoreCase(args[0]) || "-pagerQuery".equalsIgnoreCase(args[0])) {
            codeGenerator.pagerQuery(po);
            return;
        }

        if ("-cq".equalsIgnoreCase(args[0]) || "-countQuery".equalsIgnoreCase(args[0])) {
            codeGenerator.countQuery(po);
            return;
        }

        if ("-bop".equalsIgnoreCase(args[0]) || "-BatchOperateParam".equalsIgnoreCase(args[0])) {
            codeGenerator.batchOperate(po);
            return;
        }

        if ("-d".equalsIgnoreCase(args[0]) || "-dao".equalsIgnoreCase(args[0])) {
            codeGenerator.dao(po);
            return;
        }

        if ("-r".equalsIgnoreCase(args[0]) || "-repository".equalsIgnoreCase(args[0])) {
            codeGenerator.repository(po);
            return;
        }

        if ("-di".equalsIgnoreCase(args[0]) || "-daoImpl".equalsIgnoreCase(args[0])) {
            codeGenerator.daoImpl(po);
            return;
        }

        if ("-mi".equalsIgnoreCase(args[0]) || "-mybatisImpl".equalsIgnoreCase(args[0])) {
            codeGenerator.daoMybatis(po);
            return;
        }

        if ("-ri".equalsIgnoreCase(args[0]) || "-repositoryImpl".equalsIgnoreCase(args[0])) {
            codeGenerator.repositoryImpl(po);
            return;
        }

        if ("-s".contentEquals(args[0]) || "-service".equalsIgnoreCase(args[0])) {
            codeGenerator.service(po);
            return;
        }

        if ("-c".contentEquals(args[0]) || "-controller".equalsIgnoreCase(args[0])) {
            codeGenerator.controller(po);
            return;
        }

        if ("-ct".equals(args[0]) || "-createDDL".equalsIgnoreCase(args[0])) {
            codeGenerator.generaCreateDDL(po);
        }
    }

    public static void main(String[] args) throws Exception {
        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder().initController(false)
            .initInterceptor(false));
//        args = "--example".split(" ");
//        args = "-mi com.sparrow.example.po.SparrowExample -config=/Users/zhanglizhi/workspace/tedu/tarena-tp-basic/basic-po/bin/config.properties".split(" ");

        if (args.length == 0 || "--help".equals(args[0])) {
            usage();
            return;
        }
        if ("--example".equals(args[0])) {
            List<String> argsList = new ArrayList<>(args.length);
            argsList.add("-c com.sparrow.example.po.SparrowExample");
            argsList.add("-b com.sparrow.example.po.SparrowExample");
            argsList.add("-p com.sparrow.example.po.SparrowExample");
            argsList.add("-q com.sparrow.example.po.SparrowExample");
            argsList.add("-v com.sparrow.example.po.SparrowExample");
            argsList.add("-cv com.sparrow.example.po.SparrowExample");
            argsList.add("-d com.sparrow.example.po.SparrowExample");
            argsList.add("-di com.sparrow.example.po.SparrowExample");
            argsList.add("-r com.sparrow.example.po.SparrowExample");
            argsList.add("-ri com.sparrow.example.po.SparrowExample");
            argsList.add("-s com.sparrow.example.po.SparrowExample");
            argsList.add("-a com.sparrow.example.po.SparrowExample");
            argsList.add("-ct com.sparrow.example.po.SparrowExample");
            argsList.add("-pq com.sparrow.example.po.SparrowExample");
            argsList.add("-cq com.sparrow.example.po.SparrowExample");
            argsList.add("-bop com.sparrow.example.po.SparrowExample");

            for (String argsLine : argsList) {
                args = argsLine.split(" ");
                innerMain(args);
            }
            return;
        }

        if (args.length < 2) {
            usage();
            return;
        }
        innerMain(args);
    }
}
