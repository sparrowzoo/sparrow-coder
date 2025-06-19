package com.sparrowzoo.coder.domain.service.frontend;

import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.io.file.FileNameProperty;
import com.sparrow.orm.Field;
import com.sparrow.utility.FileUtility;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.FrontendKey;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import javax.print.DocFlavor;
import java.io.File;
import java.util.Map;
import java.util.Properties;

public class DefaultFrontendPlaceholder implements FrontendPlaceholder {
    protected final TableConfigRegistry registry;
    protected final TableContext tableContext;
    protected final Properties config;

    public DefaultFrontendPlaceholder(TableConfigRegistry registry, String tableName) {
        this.registry = registry;
        this.config = registry.getProjectConfig();
        this.tableContext = registry.getTableContext(tableName);
    }

    @Override
    public String getPath(FrontendKey key) {
        String originPath = this.config.getProperty(key.name().toLowerCase());
        String persistenceObjectByDot = tableContext.getPlaceHolder().get(PlaceholderKey.$persistence_object_by_horizontal.name());
        originPath = originPath.replace(PlaceholderKey.$persistence_object_by_horizontal.name(), persistenceObjectByDot);
        FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty(originPath);
        return new FileNameBuilder(fileNameProperty.getName().replace(".", File.separator)).extension(fileNameProperty.getExtension()).build();
    }

    @Override
    public void init() {
        Map<String, String> placeholder = tableContext.getPlaceHolder();
        placeholder.put(PlaceholderKey.$frontend_page.name(), this.getPath(FrontendKey.PAGE));
        placeholder.put(PlaceholderKey.$frontend_api.name(), this.getPath(FrontendKey.API));
        placeholder.put(PlaceholderKey.$frontend_add.name(), this.getPath(FrontendKey.ADD));
        placeholder.put(PlaceholderKey.$frontend_edit.name(), this.getPath(FrontendKey.EDIT));
        placeholder.put(PlaceholderKey.$frontend_search.name(), this.getPath(FrontendKey.SEARCH));
        placeholder.put(PlaceholderKey.$frontend_columns.name(), this.getPath(FrontendKey.COLUMNS));
        placeholder.put(PlaceholderKey.$frontend_schema.name(), this.getPath(FrontendKey.SCHEMA));
        placeholder.put(PlaceholderKey.$frontend_operation.name(), this.getPath(FrontendKey.OPERATION));

        /**
         * id: number | string;
         *     amount: number;
         *     status: "pending" | "processing" | "success" | "failed";
         *     email: string;
         *     currency?: string;
         */
        Map<String, Field> fields = tableContext.getEntityManager().getPropertyFieldMap();
        StringBuilder fieldBuild = new StringBuilder();
        for (Field field : fields.values()) {
            Class<?> fieldClazz = field.getType();
            String property = String.format("%s:%s; \n", field.getPropertyName(), this.toType(fieldClazz));
            fieldBuild.append(property);
        }
        placeholder.put(PlaceholderKey.$frontend_vo.name(), fieldBuild.toString());
        placeholder.put(PlaceholderKey.$frontend_column_filter.name(),tableContext.getTableConfig().getColumnFilter().toString());
    }

    private String toType(Class<?> clazz) {
        if (Number.class.isAssignableFrom(clazz)) {
            return "number";
        }
        if (Boolean.class.isAssignableFrom(clazz)) {
            return "boolean";
        }
        return "string";
    }
}
