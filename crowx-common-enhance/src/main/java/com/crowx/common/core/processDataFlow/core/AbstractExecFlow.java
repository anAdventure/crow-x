package com.crowx.common.core.processDataFlow.core;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.crowx.common.core.processDataFlow.core.api.*;
import com.crowx.common.core.processDataFlow.domain.FieldDefinition;
import com.crowx.common.core.processDataFlow.exception.ExcelImportException;
import com.crowx.common.core.processDataFlow.handler.DataListHandler;
import com.crowx.common.core.processDataFlow.handler.DataRowHandler;
import com.crowx.common.core.processDataFlow.resolver.DataListResolver;
import com.crowx.common.utils.BeanUtilsBuffer;
import com.google.common.collect.Maps;
import io.jsonwebtoken.lang.Assert;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class AbstractExecFlow<T> implements ExecFlow<T> {

    /**
     * 全局上下文
     */
    protected ExecDataContext<T> context;

    /**
     * 数据处理器
     */
    private List<ExecFlowStep> execFlowStepList;

    /**
     * 集合数据处理器
     */
    private List<ExecListFlowStep> execListFlowStepList;

    /**
     * 行数据处理器
     */
    private List<ExecRowFlowStep> execRowFlowStepList;

    /**
     * 数据解析器
     */
    private DataListResolver<T> dataListResolver;

    public AbstractExecFlow(ExecDataContext<T> context) {
        this.context = context;
    }

    private void sendContext(List<?> execFlowStepList) {
        execFlowStepList.stream()
                .filter(s->s instanceof ExecDataContextAware)
                .map(s->(ExecDataContextAware<T>)s)
                .forEach(s->s.setContext(context));
    }

    @Override
    public ExecDataContext<T> getContext() {
        return this.context;
    }

    @Override
    public List<T> executeFlow() throws ExcelImportException {
        List<T> list = execute();

        // 执行监听器

        return list;
    }

    private List<T> execute() throws ExcelImportException {
        prepareRefresh();

        List<T> dataList = null;
        try {
            // 加载数据
            dataList = dataListResolver.loadData(context);
        } catch (ExcelImportException e) {
            throw new ExcelImportException(e.getFailList());
        }

        dataList=postDataCheck(dataList);

        List<T> resultDataList = new ArrayList<>();

        // 执行行处理器
        for (T dataItem : dataList) {
            resultDataList.add(executeRowSteps(dataItem));
        }
        // 执行集合处理器
        resultDataList= executeListSteps(resultDataList);


        return resultDataList;
    }

    private List<T> executeListSteps(List<T> resultDataList) {
        for (ExecListFlowStep execListFlowStep : this.execListFlowStepList) {
            if(execListFlowStep instanceof DataListHandler){
               resultDataList=((DataListHandler<T>) execListFlowStep).handler(resultDataList);
            }
        }
        return resultDataList;
    }

    private T executeRowSteps(T bean) {
        for (ExecRowFlowStep execRowFlowStep : this.execRowFlowStepList) {
            if(execRowFlowStep instanceof DataRowHandler){
                bean = ((DataRowHandler<T>) execRowFlowStep).handler(bean);
            }
        }
        return bean;
    }

    T newBean(){
        Supplier<T> beanCreate = context.getBeanCreate();
        return beanCreate.get();
    }
    private void setFieldValue(FieldDefinition<T, ?> field, T bean, Object val) {
        invokeSetter(field.getSetter(),bean,val);


    }
    @SuppressWarnings("all")
    protected <FT> void invokeSetter(BiConsumer<T, ?> setter, T bean, FT val) {

        if (val == null) {
            return;
        }

        BiConsumer<T, FT> typeSetter = (BiConsumer<T, FT>) setter;
        typeSetter.accept(bean, val);
    }

    /**
     *  子类处理数据
     */
    protected abstract List<T> postDataCheck(List<T> dataList);


    private void prepareRefresh() {
        this.execFlowStepList= Optional.ofNullable(context.getExecFlowStepList()).orElse(Collections.emptyList());
        sendContext(execFlowStepList);

        this.execRowFlowStepList = execFlowStepList.stream().filter(s->s instanceof ExecRowFlowStep).map(s->(ExecRowFlowStep)s).collect(Collectors.toList());
        this.execListFlowStepList = execFlowStepList.stream().filter(s->s instanceof ExecListFlowStep).map(s->(ExecListFlowStep)s).collect(Collectors.toList());

        this.dataListResolver=context.getDataListResolver();
        Assert.notNull(dataListResolver,"数据解析器为空!");
    }


}
