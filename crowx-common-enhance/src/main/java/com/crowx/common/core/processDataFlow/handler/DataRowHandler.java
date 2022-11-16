package com.crowx.common.core.processDataFlow.handler;

import com.crowx.common.core.processDataFlow.core.api.ExecListFlowStep;

import java.util.List;

/**
 * 集合数据处理器
 * @param <T>
 */
public interface DataRowHandler<T> extends DataHandler, ExecListFlowStep {
    T handler(T bean);
}
