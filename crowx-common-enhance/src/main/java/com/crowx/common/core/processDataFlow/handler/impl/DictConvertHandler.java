package com.crowx.common.core.processDataFlow.handler.impl;

import com.crowx.common.core.processDataFlow.core.ExecDataContext;
import com.crowx.common.core.processDataFlow.core.api.ExecDataContextAware;
import com.crowx.common.core.processDataFlow.handler.DataListHandler;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public class DictConvertHandler<T> implements DataListHandler<T>, ExecDataContextAware<T> {

    private ExecDataContext<T> context;
    @Override
    public List<T> handler(List<T> list) {
        // 系统字典
        // DictConvertData接口返回自定义的数据字典
        return list;
    }

    public DictConvertHandler(Boolean codeToValue) {
    }


    public DictConvertHandler() {
        this(true);
    }

    @Override
    public void setContext(ExecDataContext<T> execDataContext) {
     this.context=execDataContext;
    }
}
