package com.ruoyi.test.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.crowx.common.annotation.Excel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 【请填写功能名称】对象 t_user
 * 
 * @author ruoyi
 * @date 2022-03-19
 */
@Data
@TableName("t_user")
public class TUser
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String username;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String password;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String sex;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String deleted;
    private LocalDateTime createTime;

}
