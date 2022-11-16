package com.crowx.common.core.processDataFlow.core;

import java.util.List;

public class DefaultExecFlow<T> extends AbstractExecFlow<T>{

    public DefaultExecFlow(ExecDataContext<T> context) {
        super(context);
    }

    @Override
    protected List<T> postDataCheck(List<T> dataList) {
        return dataList;
    }

}
