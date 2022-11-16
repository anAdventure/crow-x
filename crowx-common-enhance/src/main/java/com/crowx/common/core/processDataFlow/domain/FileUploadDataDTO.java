package com.crowx.common.core.processDataFlow.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 类 名: FileUploadDataDTO
 * 描 述: 文件上传DTO
 * 作 者: 张强
 * 创 建：2021年01月31日
 * 版 本：v1.0.0
 *
 * 历 史: (版本) 作者 时间 注释
 */
@Data
public class FileUploadDataDTO {

    /** 文件名称 */
    @NotEmpty(message = "文件名称不能为空")
    private String fileName;
    /** 文件路径 */
    @NotEmpty(message = "文件路径不能为空")
    private String filePath;
    /** 上传编码 */
    private String uploadCode;

}
