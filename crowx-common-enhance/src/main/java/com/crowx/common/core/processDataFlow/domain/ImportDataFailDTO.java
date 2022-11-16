package com.crowx.common.core.processDataFlow.domain;

import lombok.Data;
import lombok.ToString;

/**
 * 类 名: ImportDataFailDTO
 * 描 述: 导入数据失败记录DTO
 * 版 本：v1.0.0
 *
 * 历 史: (版本) 作者 时间 注释
 */
@Data
@ToString
public class ImportDataFailDTO {

    /** 行号 */
    private Integer rowNo;
    /** 列号 */
    private Integer colNo;
    /** 错误类型 */
    private String errorType;
    /** 错误值 */
    private String errorValue;

    public ImportDataFailDTO() {
    }

    public ImportDataFailDTO(String errorType, String errorValue) {
        this.errorType = errorType;
        this.errorValue = errorValue;
    }

    public ImportDataFailDTO(Integer rowNo, Integer colNo, String errorType, String errorValue) {
        this.rowNo = rowNo;
        this.colNo = colNo;
        this.errorType = errorType;
        this.errorValue = errorValue;
    }
}
