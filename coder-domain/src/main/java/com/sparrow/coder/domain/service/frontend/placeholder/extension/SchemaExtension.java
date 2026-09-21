package com.sparrow.coder.domain.service.frontend.placeholder.extension;

import com.sparrow.utility.CollectionsUtility;
import com.sparrow.coder.domain.bo.ColumnDef;
import com.sparrow.coder.domain.bo.ProjectBO;
import com.sparrow.coder.domain.bo.ProjectConfigBO;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.bo.validate.Validator;
import com.sparrow.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrow.coder.domain.service.ArchitectureGenerator;
import com.sparrow.coder.domain.service.ValidatorMessageGenerator;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.domain.service.registry.ValidatorRegistry;
import com.sparrow.coder.enums.ArchitectureCategory;
import com.sparrow.coder.enums.ColumnType;
import com.sparrow.coder.enums.PlaceholderKey;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
public class SchemaExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        ProjectBO project = tableContext.getProject();
        List<ColumnDef> columnDefs = tableContext.getColumns();
        if (CollectionsUtility.isNullOrEmpty(columnDefs)) {
            return;
        }
        ArchitectureGenerator architectureGenerator = project.getArchitecture(ArchitectureCategory.FRONTEND);
        String architectureName = architectureGenerator.getName();
        Map<String, ValidatorMessageGenerator> validateContainer = ValidatorRegistry.getInstance().getRegistry().get(architectureName);
        Map<String, String> placeholder = tableContext.getPlaceHolder();
        placeholder.put(PlaceholderKey.$frontend_schema.name(), this.generateSchema(tableContext, project, validateContainer));
    }

    private String generateSchema(TableContext tableContext, ProjectBO project, Map<String, ValidatorMessageGenerator> validateContainer) {
        List<ColumnDef> columnDefs = tableContext.getColumns();
        List<String> schemas = new ArrayList<>();
        ProjectConfigBO projectConfig = project.getProjectConfig();
        for (ColumnDef columnDef : columnDefs) {
            if (!columnDef.getShowInEdit()) {
                continue;
            }
            ColumnType columnType = ColumnType.getById(columnDef.getColumnType());
            if (ColumnType.ACTION.equals(columnType) ||
                    ColumnType.CHECK.equals(columnType) ||
                    ColumnType.FILTER.equals(columnType)
            ) {
                continue;
            }
            String validateType = columnDef.getValidateType() == null ? "nullable" : columnDef.getValidateType();
            ValidatorMessageGenerator messageGenerator = validateContainer.get(validateType);
            Validator validator = columnDef.getValidator();
            if (validator == null) {
                validator = messageGenerator.defaultValidator();
            }
            Map<String, String> columnI18nMap = tableContext.getValidateI18nMap(columnDef.getPropertyName());
            validator.setI18nConfig(columnI18nMap);
            validator.setI18n(projectConfig.getI18n());
            schemas.add(messageGenerator.generateConfig(columnDef.getPropertyName(), validator));
        }
        return String.join(",", schemas);
    }
}
