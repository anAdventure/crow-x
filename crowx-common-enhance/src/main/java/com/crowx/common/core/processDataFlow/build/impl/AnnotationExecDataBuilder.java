package com.crowx.common.core.processDataFlow.build.impl;

import com.crowx.common.core.processDataFlow.build.AbstractFieldDefinitionBuilder;
import com.crowx.common.annotation.Excel;
import com.crowx.common.utils.reflect.ReflectUtils;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnnotationExecDataBuilder<T> extends AbstractFieldDefinitionBuilder<T,AnnotationExecDataBuilder.Param> {

    private Class<T> beanClass;

    public AnnotationExecDataBuilder(Class<T> tClass) {
        this.beanClass = tClass;
    }

    @Override
    public BiConsumer<T, Object> getSetter(Param param) {
        String fieldName = param.getField().getName();
        return (obj,value)->ReflectUtils.invokeSetter(obj,fieldName,value);
    }

    @Override
    public Function<T, Object> getGetter(Param param) {

         String fieldName = param.getField().getName();
        return (obj)->ReflectUtils.invokeGetter(obj,fieldName);
    }

    @Override
    public Class<?> getFieldType(Param param) {
        return param.getField().getType();
    }

    @Override
    public String getAlias(Param param) {
        return param.getExcel().name();
    }

    @Override
    public Map<String, Object> getOtherDesc(Param param) {
        return null;
    }

    @Override
    public List<Param> getImportFieldList() {
        return Arrays.stream(beanClass.getDeclaredFields())
                .filter(s-> !ObjectUtils.isEmpty(s.getAnnotation(Excel.class)))
                .map(s-> new Param(s,s.getAnnotation(Excel.class)))
                .collect(Collectors.toList());
    }

    @Data
    static class Param {

        private Field field;

        private Excel excel;

        public Param(Field field, Excel excel) {
            this.field = field;
            this.excel = excel;
        }
    }
}
