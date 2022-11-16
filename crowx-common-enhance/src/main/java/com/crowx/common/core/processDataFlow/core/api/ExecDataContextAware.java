package com.crowx.common.core.processDataFlow.core.api;

import com.crowx.common.core.processDataFlow.core.ExecDataContext;
import org.apache.poi.ss.formula.functions.T;

public interface ExecDataContextAware<T> {

    void setContext(ExecDataContext<T> execDataContext);
}
