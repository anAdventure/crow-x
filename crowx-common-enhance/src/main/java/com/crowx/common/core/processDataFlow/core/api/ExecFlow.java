package com.crowx.common.core.processDataFlow.core.api;

import com.crowx.common.core.processDataFlow.core.ExecDataContext;
import com.crowx.common.core.processDataFlow.exception.ExcelImportException;

import java.util.List;

public interface ExecFlow<T> {

    List<T> executeFlow() throws ExcelImportException;

    ExecDataContext<T> getContext();
}
