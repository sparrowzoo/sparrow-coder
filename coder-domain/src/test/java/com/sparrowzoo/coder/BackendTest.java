package com.sparrowzoo.coder;

import com.sparrowzoo.coder.domain.DomainRegistry;
import com.sparrowzoo.coder.domain.service.CodeGenerator;
import com.sparrowzoo.coder.domain.service.EnvConfig;
import com.sparrowzoo.coder.domain.service.DefaultCodeGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.io.IOException;

@SpringBootTest
public class BackendTest {
    @Autowired
    EnvConfig envConfig;

    @Inject
    private DomainRegistry domainRegistry;

    @Test
    public void classTest() {
    }

    @Test
    public void generate() throws IOException, ClassNotFoundException {
        CodeGenerator generator = new DefaultCodeGenerator(1L, envConfig, domainRegistry);
        //generator.clear();
        //generator.initScaffold();
        generator.generate("t_table_config");
        generator.generate("t_project_config");
    }
}
