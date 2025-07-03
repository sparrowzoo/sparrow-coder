package com.sparrowzoo.coder.domain.service.frontend;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.io.file.FileNameProperty;
import com.sparrow.json.Json;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.ProjectBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.enums.FrontendKey;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import java.io.File;
import java.util.List;

public class DefaultFrontendPlaceholder implements FrontendPlaceholderGenerator {
    protected final ProjectBO project;
    protected final TableContext tableContext;
    protected List<ColumnDef> columnDefs;
    protected Json json = JsonFactory.getProvider();

    public DefaultFrontendPlaceholder(ProjectBO project, TableContext tableContext) {
        this.project = project;
        this.tableContext = tableContext;
        this.columnDefs = tableContext.getColumns();
    }

    @Override
    public String getPath(FrontendKey key) {
        String originPath = this.project.getScaffoldConfig().getProperty(key.name().toLowerCase());
        String persistenceClassName = tableContext.getEntityManager().getSimpleClassName();
        String persistenceObjectByDot = StringUtility.humpToLower(persistenceClassName, '-');
        originPath = originPath.replace(PlaceholderKey.$persistence_object_by_horizontal.name(), persistenceObjectByDot);
        originPath = originPath.replace(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
        FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty(originPath);
        return new FileNameBuilder(fileNameProperty.getName().replace(".", File.separator)).extension(fileNameProperty.getExtension()).build();
    }
}
