package com.sparrow.coding.generate;

import com.sparrow.coding.DigitalCategory;
import com.sparrow.coding.api.CodeGenerator;
import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.api.ValidatorRegistry;
import com.sparrow.coding.java.DefaultCodeGenerator;
import com.sparrow.coding.protocol.validate.DigitalValidator;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class BackendTest {
    @Test
    public void generate() throws IOException, ClassNotFoundException {
        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder().initController(false)
                .initInterceptor(false)
                .scanBasePackage("com.sparrow"));

        CodeGenerator generator = new DefaultCodeGenerator(1L);
        //generator.initScaffold();
        generator.generate("t_table_config");
    }
}
