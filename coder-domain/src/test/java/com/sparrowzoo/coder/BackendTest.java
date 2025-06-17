package com.sparrowzoo.coder;

import com.sparrow.protocol.dao.PO;
import com.sparrowzoo.coder.domain.DomainRegistry;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.domain.service.CodeGenerator;
import com.sparrowzoo.coder.domain.service.EnvConfig;
import com.sparrowzoo.coder.domain.service.backend.DefaultCodeGenerator;
import com.sparrowzoo.coder.po.TableConfig;
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
        Class clazz = TableConfig.class;
        System.out.println(PO.class.isAssignableFrom(clazz));
    }

    @Test
    public void generate() throws IOException, ClassNotFoundException {
        CodeGenerator generator = new DefaultCodeGenerator(1L, envConfig, domainRegistry);
        //generator.clear();
//        generator.initScaffold();
        generator.generate("t_table_config");
        generator.generate("t_project_config");

    }
}
