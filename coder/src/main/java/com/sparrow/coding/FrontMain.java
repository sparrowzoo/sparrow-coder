package com.sparrow.coding;

import com.sparrow.coding.frontend.EnvironmentContext;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.POJO;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class FrontMain {
    private static void usage() {
        System.out.println("usage:");
        System.out.println("sparrow-front-coder.sh [OPTION] [args]");
        System.out.println("OPTION:");
        System.out.println("generate manage page:  [-mp |-GenerateManagePage]  args=pojo");
        System.out.println("generate create page:  [-cp |-GenerateCreatePage]  args=pojo");
        System.out.println("generate create js:    [-cj |-GenerateLanguageJs]  args=pojo");
        System.out.println("generate language js:  [-lj |-GenerateLanguageJs]  args=pojo");
    }

    public static void main(
        String[] args) throws ClassNotFoundException, IOException, ParserConfigurationException, SAXException {
        args = "-mp com.sparrow.coding.config.ExampleUser".split(" ");

        if (args.length == 0 || "--help".equals(args[0])) {
            usage();
            System.exit(0);
        }
        if (args.length < 2) {
            usage();
            System.exit(0);
        }

        Class<? extends POJO> clazz = (Class<? extends POJO>) Class.forName(args[1]);
        EnvironmentContext environmentContext = new EnvironmentContext();
        EnvironmentContext.Config config = environmentContext.new Config(clazz);
        if ("-mp".equals(args[0]) || "-GenerateManagePage".equals(args[0])) {
            config.generateManagePage();
            System.exit(0);
        }

        if ("-cp".equals(args[0]) || "-GenerateCreatePage".equals(args[0])) {
            config.generateCreatePage();
            System.exit(0);
        }

        if ("-cj".equals(args[0]) || "-GenerateCreateJs".equals(args[0])) {
            config.generateCreateJs();
            System.exit(0);
        }

        if ("-lj".equals(args[0]) || "-GenerateLanguageJs".equals(args[0])) {
            config.generateLanguageJs();
            System.exit(0);
        }
    }
}
