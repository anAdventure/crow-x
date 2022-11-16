package com.crowx.common.core.processDataFlow.build;

import com.crowx.common.core.processDataFlow.domain.FieldDefinition;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractFieldDefinitionBuilder<T, F> implements FieldDefinitionBuilder<T> {



    /**
     * 实体类 字段 setter
     */
    public abstract BiConsumer<T, Object> getSetter(F f);

    /**
     * 实体类 字段 getter
     */
    public abstract Function<T, Object> getGetter(F f);

    /**
     * 字段类型
     */
    public abstract Class<?> getFieldType(F f);

    /**
     * Excel 表头名称
     */
    public abstract String getAlias(F f);

    /**
     * 获取字段的其他信息
     */
    public abstract Map<String, Object> getOtherDesc(F f);

    /**
     * 获取字段描述集合
     *
     * @return
     */
    public abstract List<F> getImportFieldList();

    @Override
    public List<FieldDefinition<T, ?>> build() {
        return getImportFieldList().stream().map(f -> {
            FieldDefinition<T, Object> definition = new FieldDefinition<>();
            definition.setSetter(getSetter(f));
            definition.setGetter(getGetter(f));
            definition.setFieldType(getFieldType(f));
            definition.setAlias(getAlias(f));
            definition.setOtherDesc(getOtherDesc(f));
            return definition;
        }).collect(Collectors.toList());
    }
}
