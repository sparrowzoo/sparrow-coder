package com.sparrowzoo.coder.domain.service.frontend.placeholder.extension;

import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrowzoo.coder.domain.service.frontend.FrontendPlaceholderGenerator;
import com.sparrowzoo.coder.enums.FrontendKey;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import javax.inject.Named;
import java.util.Map;

@Named
public class PathPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext) {
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
