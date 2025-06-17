package com.sparrowzoo.coder.generate;

import com.sparrow.coding.api.CodeGenerator;
import com.sparrow.coder.java.DefaultCodeGenerator;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrowzoo.coder.service.CodeGenerator;
import com.sparrowzoo.coder.service.backend.DefaultCodeGenerator;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class BackendTest {
    @Test
    public void generate() throws IOException, ClassNotFoundException {
        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder().initController(false)
                .initInterceptor(false)
                .scanBasePackage("com.sparrow"));

        CodeGenerator generator = new DefaultCodeGenerator(1L,);
        generator.initScaffold();
        generator.generate("t_table_config");
    }
}
