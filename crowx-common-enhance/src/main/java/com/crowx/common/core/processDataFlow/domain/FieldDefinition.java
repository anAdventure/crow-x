package com.crowx.common.core.processDataFlow.domain;

import lombok.Data;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Data
public class FieldDefinition<T, R> {

    /**
     * 实体类别名
     */
    private String alias;

    /**
     * 实体类 字段 setter
     */
    private BiConsumer<T, R> setter;

    /**
     * 实体类 字段 getter
     */
    private Function<T, R> getter;

    /**
     * 字段类型
     */
    private Class<?> fieldType;


    private Map<String,Object> otherDesc;
}
