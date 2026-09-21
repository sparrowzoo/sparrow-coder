package com.sparrow.coder.domain.service.frontend.placeholder.extension;

import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrow.coder.domain.service.frontend.FrontendPlaceholderGenerator;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.enums.FrontendKey;
import com.sparrow.coder.enums.PlaceholderKey;
import jakarta.inject.Named;

import java.util.Map;

@Named
public class PathPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        FrontendPlaceholderGenerator frontendPlaceholderGenerator = tableContext.getFrontendPlaceholderGenerator();
        Map<String, String> placeholder = tableContext.getPlaceHolder();
        placeholder.put(PlaceholderKey.$frontend_path_page.name(), frontendPlaceholderGenerator.getPath(FrontendKey.PAGE));
        placeholder.put(PlaceholderKey.$frontend_path_api.name(), frontendPlaceholderGenerator.getPath(FrontendKey.API));
        placeholder.put(PlaceholderKey.$frontend_path_add.name(), frontendPlaceholderGenerator.getPath(FrontendKey.ADD));
        placeholder.put(PlaceholderKey.$frontend_path_edit.name(), frontendPlaceholderGenerator.getPath(FrontendKey.EDIT));
        placeholder.put(PlaceholderKey.$frontend_path_search.name(), frontendPlaceholderGenerator.getPath(FrontendKey.SEARCH));
        placeholder.put(PlaceholderKey.$frontend_path_columns.name(), frontendPlaceholderGenerator.getPath(FrontendKey.COLUMNS));
        placeholder.put(PlaceholderKey.$frontend_path_schema.name(), frontendPlaceholderGenerator.getPath(FrontendKey.SCHEMA));
        placeholder.put(PlaceholderKey.$frontend_path_operation.name(), frontendPlaceholderGenerator.getPath(FrontendKey.OPERATION));
    }
}
