package com.sparrow.coding;

import com.sparrow.coding.frontend.EnvironmentContext;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.POJO;
import com.sparrow.utility.StringUtility;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class FrontMain {
    private static void usage() {
        System.out.println("使用方法【usage】");
        System.out.println("sparrow-front-coder.sh [OPTION] [args]");
        System.out.println("OPTION:");
        System.out.println("生成管理页:                   [-mp |-GenerateManagePage]  args=pojo");
        System.out.println("生成新建页:                   [-cp |-GenerateCreatePage]  args=pojo");
        System.out.println("生成新建页js:                 [-cj |-GenerateCreateJs]    args=pojo");
        System.out.println("生成国际化提示信息【中文】js:    [-lj |-GenerateLanguageJs]  args=pojo");
        System.out.println("生成管理页 js:                [-mj |-GenerateManageJs]    args=pojo");
    }

    public static void innerMain(
        String[] args) throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException {
        System.out.println("生成类:" + args[1]);
        Class<? extends POJO> clazz = (Class<? extends POJO>) Class.forName(args[1]);
        EnvironmentContext environmentContext = new EnvironmentContext();
        EnvironmentContext.Config config = environmentContext.new Config(clazz);
        if ("-mp".equals(args[0]) || "-GenerateManagePage".equals(args[0])) {
            config.generateManagePage();
            return;
        }

        if ("-cp".equals(args[0]) || "-GenerateCreatePage".equals(args[0])) {
            config.generateCreatePage();
            return;
        }

        if ("-cj".equals(args[0]) || "-GenerateCreateJs".equals(args[0])) {
            config.generateCreateJs();
            return;
        }

        if ("-lj".equals(args[0]) || "-GenerateLanguageJs".equals(args[0])) {
            config.generateLanguageJs();
            return;
        }

        if ("-mj".equals(args[0]) || "-GenerateManageJs".equals(args[0])) {
            config.generateManageJs();
            return;
        }
    }

    public static void main(
        String[] args) throws ClassNotFoundException, IOException, ParserConfigurationException, SAXException {
        //args = "--example".split(" ");
        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder().initController(false)
            .initInterceptor(false)
            .scanBasePackage("com.sparrow"));

        if (args.length == 0 || "--help".equals(args[0])) {
            usage();
            return;
        }

        if ("--example".equals(args[0])) {
            List<String> argsList = new ArrayList<>();
            argsList.add("-mp com.sparrow.coding.config.ExampleFront");
            argsList.add("-cp com.sparrow.coding.config.ExampleFront");
            argsList.add("-cj com.sparrow.coding.config.ExampleFront");
            argsList.add("-lj com.sparrow.coding.config.ExampleFront");
            argsList.add("-mj com.sparrow.coding.config.ExampleFront");

            for (String argLine : argsList) {
                args = argLine.split(" ");
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
