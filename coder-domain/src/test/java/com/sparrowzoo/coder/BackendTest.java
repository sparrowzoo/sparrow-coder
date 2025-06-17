package com.sparrowzoo.coder;

import com.sparrowzoo.coder.service.CodeGenerator;
import com.sparrowzoo.coder.service.EnvConfig;
import com.sparrowzoo.coder.service.backend.DefaultCodeGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class BackendTest {
    @Autowired
    EnvConfig envConfig;

    @Test
    public void generate() throws IOException, ClassNotFoundException {
        CodeGenerator generator = new DefaultCodeGenerator(1L, envConfig);
        //generator.clear();
        generator.initScaffold();
        generator.generate("t_table_config");
        //generator.generate("t_table_config");

    }
}
