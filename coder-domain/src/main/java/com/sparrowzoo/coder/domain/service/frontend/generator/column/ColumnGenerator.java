package com.sparrowzoo.coder.domain.service.frontend.generator.column;

import com.sparrow.protocol.NameAccessor;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.ProjectBO;
import com.sparrowzoo.coder.enums.CellType;
import com.sparrowzoo.coder.enums.HeaderType;

public interface ColumnGenerator extends NameAccessor {
    String column(ColumnDef columnDef,ProjectBO project);

    String edit(ColumnDef columnDef,ProjectBO project,Boolean add);

    String importHeader(HeaderType headerType,ProjectBO project);

    String importCell(CellType cellType,ProjectBO project);
}
