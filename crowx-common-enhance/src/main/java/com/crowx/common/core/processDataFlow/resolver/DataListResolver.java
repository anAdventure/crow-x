package com.crowx.common.core.processDataFlow.resolver;

import com.crowx.common.core.processDataFlow.core.ExecDataContext;
import com.crowx.common.core.processDataFlow.core.api.ExecFlowStep;
import com.crowx.common.core.processDataFlow.exception.ExcelImportException;

import java.util.List;
import java.util.Map;

/**
 * 数据解析器
 */
public interface DataListResolver<T>  extends DataResolver, ExecFlowStep {

    public List<T> loadData(ExecDataContext<T> execDataContext) throws ExcelImportException;


}
