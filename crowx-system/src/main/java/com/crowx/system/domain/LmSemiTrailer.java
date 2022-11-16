package com.crowx.system.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.crowx.common.annotation.Excel;
import com.crowx.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 半挂对象 lm_semi_trailer
 * 
 * @author xiezihao
 * @date 2022-10-15
 */
public class LmSemiTrailer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 毛重 */
    @Excel(name = "毛重")
    private BigDecimal grossWeight;

    /** 皮重 */
    @Excel(name = "皮重")
    private BigDecimal tareWeight;

    /** 净重=毛重-皮重 */
    @Excel(name = "净重=毛重-皮重")
    private BigDecimal netWeight;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String carNumber;

    /** 总价格 */
    @Excel(name = "总价格")
    private BigDecimal allPrice;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setGrossWeight(BigDecimal grossWeight) 
    {
        this.grossWeight = grossWeight;
    }

    public BigDecimal getGrossWeight() 
    {
        return grossWeight;
    }
    public void setTareWeight(BigDecimal tareWeight) 
    {
        this.tareWeight = tareWeight;
    }

    public BigDecimal getTareWeight() 
    {
        return tareWeight;
    }
    public void setNetWeight(BigDecimal netWeight) 
    {
        this.netWeight = netWeight;
    }

    public BigDecimal getNetWeight() 
    {
        return netWeight;
    }
    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }
    public void setCarNumber(String carNumber) 
    {
        this.carNumber = carNumber;
    }

    public String getCarNumber() 
    {
        return carNumber;
    }
    public void setAllPrice(BigDecimal allPrice) 
    {
        this.allPrice = allPrice;
    }

    public BigDecimal getAllPrice() 
    {
        return allPrice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("grossWeight", getGrossWeight())
            .append("tareWeight", getTareWeight())
            .append("netWeight", getNetWeight())
            .append("price", getPrice())
            .append("carNumber", getCarNumber())
            .append("allPrice", getAllPrice())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
