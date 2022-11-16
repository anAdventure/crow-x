package com.crowx.common.core.processDataFlow.resolver.impl;

import com.crowx.common.core.processDataFlow.core.ExecDataContext;
import com.crowx.common.core.processDataFlow.exception.ExcelImportException;
import com.crowx.common.core.processDataFlow.resolver.DataListResolver;
import com.crowx.common.utils.BeanUtilsBuffer;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleDataListResolver<T> implements DataListResolver<T> {

    private List<T> list;

    public SimpleDataListResolver(List<T> list) {
        this.list = list;
    }

    @Override
    public List<T> loadData(ExecDataContext<T> execDataContext)  throws ExcelImportException {


        return list;
    }



}
