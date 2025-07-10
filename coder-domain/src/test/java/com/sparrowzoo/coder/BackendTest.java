package com.sparrowzoo.coder;

import com.alibaba.fastjson.JSON;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.pager.PagerResult;
import com.sparrow.spring.starter.EnumsContainer;
import com.sparrowzoo.coder.constant.EnumNames;
import com.sparrowzoo.coder.domain.DomainRegistry;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.CodeGenerator;
import com.sparrowzoo.coder.domain.service.EnvConfig;
import com.sparrowzoo.coder.domain.service.DefaultCodeGenerator;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.PlaceholderKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class BackendTest {
    @Autowired
    EnvConfig envConfig;

    @Inject
    private EnumsContainer coderEnumsContainer;

    @Inject
    private DomainRegistry domainRegistry;

    @Test
    public void classTest() throws IOException, ClassNotFoundException {
        CodeGenerator generator = new DefaultCodeGenerator(1L, envConfig, domainRegistry);
        TableConfigRegistry registry = generator.getRegistry();
        TableContext tableContext = registry.getTableContext("t_column_config");
        List<ColumnDef> columnDefs = tableContext.getOriginalColumns();
        PagerResult<ColumnDef> pagerResult = new PagerResult<>();
        pagerResult.setList(columnDefs);
        pagerResult.setRecordTotal((long)columnDefs.size());
        pagerResult.setPageNo(1);
        pagerResult.putDictionary(EnumNames.CELL_TYPE,coderEnumsContainer.getEnums(EnumNames.CELL_TYPE));
        pagerResult.putDictionary(EnumNames.HEADER_TYPE,coderEnumsContainer.getEnums(EnumNames.HEADER_TYPE));
        pagerResult.putDictionary(EnumNames.DATASOURCE_TYPE,coderEnumsContainer.getEnums(EnumNames.DATASOURCE_TYPE));
        pagerResult.putDictionary(EnumNames.COLUMN_TYPE,coderEnumsContainer.getEnums(EnumNames.COLUMN_TYPE));

        Result<PagerResult<ColumnDef>> result =new Result<PagerResult<ColumnDef>>(pagerResult);
//
        System.out.println(JSON.toJSONString(result));

//        Map<String, String> map = registry.getTableContext("t_column_config").getPlaceHolder();
//        System.out.println(map.get(PlaceholderKey.$frontend_i18n_message.name()));

//        System.out.println(map.get(PlaceholderKey.$frontend_class.name()));
//        System.out.println(map.get(PlaceholderKey.$frontend_column_defs.name()));
//        System.out.println(map.get(PlaceholderKey.$frontend_schema.name()));
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
