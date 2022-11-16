package com.ruoyi.test.domain;

import com.crowx.common.annotation.Excel;
import com.crowx.common.core.domain.TreeEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 护理项目对象Vo sys_nursing_items
 * 
 * @author admin
 * @date 2019-10-27
 */
@Data
public class SysNursingItemsVo extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long rowId;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 服务项名称 */
    @Excel(name = "服务项名称")
    private String itemName;

    /** 级别 */
    @Excel(name = "级别")
    private String level;

    /** 服务价格 */
    @Excel(name = "服务价格")
    private BigDecimal servicePrice;

    /** 计费单位 */
    @Excel(name = "计费单位")
    private String billingUnit;

    /** 关联租户Id */
    @Excel(name = "关联租户Id")
    private Long tenantId;

    private Long parentId;


}
