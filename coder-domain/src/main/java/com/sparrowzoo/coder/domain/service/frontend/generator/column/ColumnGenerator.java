package com.sparrowzoo.coder.domain.service.frontend.generator.column;

import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.enums.CellType;
import com.sparrowzoo.coder.enums.HeaderType;

public interface ColumnGenerator {
    String column(ColumnDef columnDef);

    String edit(ColumnDef columnDef);

    String importHeader(HeaderType headerType);

    String importCell(CellType cellType);

    String getName();
}
