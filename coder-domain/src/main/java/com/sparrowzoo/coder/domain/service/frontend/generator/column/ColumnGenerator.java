package com.sparrowzoo.coder.domain.service.frontend.generator.column;

import com.sparrowzoo.coder.domain.bo.ColumnDef;

public interface ColumnGenerator {
    String column(ColumnDef columnDef);

    String edit(ColumnDef columnDef);
}
