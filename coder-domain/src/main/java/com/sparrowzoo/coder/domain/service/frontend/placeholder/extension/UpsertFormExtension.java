package com.sparrowzoo.coder.domain.service.frontend.placeholder.extension;

import com.sparrow.utility.CollectionsUtility;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.ProjectBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrowzoo.coder.domain.service.ArchitectureGenerator;
import com.sparrowzoo.coder.domain.service.frontend.generator.column.ColumnGenerator;
import com.sparrowzoo.coder.domain.service.registry.ColumnGeneratorRegistry;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import javax.inject.Named;
import java.util.*;

@Named
public class UpsertFormExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        ProjectBO project = tableContext.getProject();
        List<ColumnDef> columnDefs = tableContext.getColumns();
        if (CollectionsUtility.isNullOrEmpty(columnDefs)) {
            return;
        }
        ColumnGeneratorRegistry columnGeneratorRegistry = ColumnGeneratorRegistry.getInstance();
        ArchitectureGenerator architectureGenerator = project.getArchitecture(ArchitectureCategory.FRONTEND);
        String architectureName = architectureGenerator.getName();
        ColumnGenerator columnGenerator = columnGeneratorRegistry.getObject(architectureName);
        this.generatorColumns(tableContext, columnGenerator, project);
    }

    private void generatorColumns(TableContext tableContext, ColumnGenerator columnGenerator, ProjectBO project) {
        List<ColumnDef> columnDefs = tableContext.getColumns();
        Map<String, String> placeholder = tableContext.getPlaceHolder();
        List<String> addFormItems = new ArrayList<>();
        List<String> editFormItems = new ArrayList<>();
        Set<String> imports=new HashSet<>();
        Map<String, Object> columnI18nMap = tableContext.getI18nMap();
        for (ColumnDef columnDef : columnDefs) {
            if (columnDef.getShowInEdit()) {
                imports.add(columnGenerator.importEdit(columnDef, project));
                addFormItems.add(columnGenerator.edit(columnDef, project, true));
                editFormItems.add(columnGenerator.edit(columnDef, project, false));
            }
            columnI18nMap.put(columnDef.getPropertyName(), columnDef.getChineseName());
        }
        String addFormItemStr = String.join("\n", addFormItems);
        String editFormItemStr = String.join("\n", editFormItems);
        placeholder.put(PlaceholderKey.$frontend_edit_import.name(), String.join("\n", imports));
        placeholder.put(PlaceholderKey.$frontend_add_form_items.name(), addFormItemStr);
        placeholder.put(PlaceholderKey.$frontend_edit_form_items.name(), editFormItemStr);
    }
}
