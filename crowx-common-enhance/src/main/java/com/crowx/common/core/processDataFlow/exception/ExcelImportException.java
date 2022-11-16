package com.crowx.common.core.processDataFlow.exception;

import com.crowx.common.core.processDataFlow.domain.ImportDataFailDTO;

import java.util.Collections;
import java.util.List;

/**
 * 类 名: ExcelImportException
 * 描 述:
 * 作 者: 谢子豪
 * 创 建：2022年04月21日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
public class ExcelImportException extends Exception {

    private List<ImportDataFailDTO> failList;

    public ExcelImportException() {
        super("文件导入失败");
        failList = Collections.singletonList(new ImportDataFailDTO("文件导入失败", null));
    }

    public ExcelImportException(String message) {
        super(message);
    }

    public ExcelImportException(ImportDataFailDTO fail) {
        super(fail.getErrorType() + "：" + fail.getErrorValue());
        failList = Collections.singletonList(fail);
    }

    public ExcelImportException(List<ImportDataFailDTO> failList) {
        super("文件解析失败：" + failList.toString());
        this.failList = failList;
    }

    public ExcelImportException(String message, Throwable cause) {
        super(message, cause);
        if (cause instanceof ExcelImportException) {
            this.failList = ((ExcelImportException) cause).getFailList();
        }
    }

    public ExcelImportException(ImportDataFailDTO fail, Throwable cause) {
        super(cause);
        failList = Collections.singletonList(fail);
        if (cause instanceof ExcelImportException) {
            this.failList.addAll(((ExcelImportException) cause).getFailList());
        }
    }

    public List<ImportDataFailDTO> getFailList() {
        return failList;
    }

}
