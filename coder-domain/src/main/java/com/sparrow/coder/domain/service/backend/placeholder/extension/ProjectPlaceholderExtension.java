package com.sparrow.coder.domain.service.backend.placeholder.extension;

import com.sparrow.utility.DateTimeUtility;
import com.sparrow.coder.domain.bo.ProjectConfigBO;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.enums.PlaceholderKey;
import jakarta.inject.Named;

import java.util.Map;

@Named
public class ProjectPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        ProjectConfigBO project = tableContext.getProject().getProjectConfig();
        Map<String, String> placeHolder = tableContext.getPlaceHolder();
        if (placeHolder.containsKey(PlaceholderKey.$project_i18n.name())) {
            return;
        }
        String modulePrefix = project.getModulePrefix();
        placeHolder.put(PlaceholderKey.$project_i18n.name(), project.getI18n().toString());
        placeHolder.put(PlaceholderKey.$date.name(), DateTimeUtility.getFormatCurrentTime("yyyy-MM-dd HH:mm:ss"));
        placeHolder.put(PlaceholderKey.$module_prefix.name(), modulePrefix);
    }
}
