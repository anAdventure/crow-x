package com.crowx.common.core.processDataFlow.build;

import com.crowx.common.core.processDataFlow.domain.FieldDefinition;

import java.util.List;

public interface FieldDefinitionBuilder<T> {

    /**
     * 构造Excel导入列配置
     *
     * @return list
     */
    List<FieldDefinition<T, ?>> build();
}
