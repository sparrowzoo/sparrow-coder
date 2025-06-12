package com.sparrow.coder.mybatis;

import com.sparrow.coder.java.EnvironmentContext;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;

import java.io.IOException;

public class MybatisEntityManagerTest {
    public static void main(String[] args) throws IOException {
        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder().initController(false)
            .initInterceptor(false)
            .scanBasePackage("com.sparrow"));
        EnvironmentContext environmentContext=new EnvironmentContext(null);
       // MybatisEntityManager entityManager=new MybatisEntityManager(SparrowExample.class,environmentContext);
       // entityManager.init();
       // System.out.println(entityManager.getXml());
    }
}
