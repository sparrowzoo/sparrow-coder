package com.sparrow.coding.mybatis;

import com.sparrow.coding.java.MybatisEntityManager;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.example.po.SparrowExample;

public class MybatisEntityManagerTest {
    public static void main(String[] args) {
        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder().initController(false)
            .initInterceptor(false)
            .scanBasePackage("com.sparrow"));
        MybatisEntityManager entityManager=new MybatisEntityManager(SparrowExample.class);
        entityManager.init();
    }
}
