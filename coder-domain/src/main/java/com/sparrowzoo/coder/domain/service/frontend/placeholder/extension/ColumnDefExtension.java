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
import com.sparrowzoo.coder.enums.CellType;
import com.sparrowzoo.coder.enums.HeaderType;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
public class ColumnDefExtension extends AbstractPlaceholderExtension {
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
        List<String> columns = new ArrayList<>();
        HashMap<String, String> imports = new HashMap<>();
        for (ColumnDef columnDef : columnDefs) {
            if (!columnDef.getShowInList()) {
                continue;
            }
            columns.add(columnGenerator.column(columnDef, project));

            if (columnDef.getHeaderType() != null) {
                HeaderType headerType = HeaderType.getById(columnDef.getHeaderType());
                if (headerType != null && !imports.containsKey(headerType.getComponentName())) {
                    imports.put(headerType.getComponentName(), columnGenerator.importHeader(headerType, project));
                }
            }

            if (columnDef.getCellType() != null) {
                CellType cellType = CellType.getById(columnDef.getCellType());
                if (cellType != null && !imports.containsKey(cellType.getComponentName())) {
                    imports.put(cellType.getComponentName(), columnGenerator.importCell(cellType, project));
                }
            }
        }
        String importStr = String.join("\n", imports.values());
        String className = tableContext.getEntityManager().getSimpleClassName();
        String frontColumnDefs = columnGenerator.columnDefs(className, columns); ;
        placeholder.put(PlaceholderKey.$frontend_column_import.name(),importStr);
        placeholder.put(PlaceholderKey.$frontend_column_defs.name(), frontColumnDefs);
    }
}
