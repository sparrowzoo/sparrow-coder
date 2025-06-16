package com.sparrowzoo.coder;

import com.sparrowzoo.coder.service.CodeGenerator;
import com.sparrowzoo.coder.service.backend.clear.DefaultCodeGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class BackendTest {
    @Test
    public void generate() throws IOException, ClassNotFoundException {
        CodeGenerator generator = new DefaultCodeGenerator(1L);
        //generator.initScaffold();
        generator.generate("t_table_config");
    }
}
