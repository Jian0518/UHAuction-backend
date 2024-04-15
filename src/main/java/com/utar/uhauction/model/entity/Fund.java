package com.utar.uhauction.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@TableName(value = "fund_distribution")
@Data
public class Fund {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "date", fill = FieldFill.INSERT)
    private Date date;

    @TableField(value = "amount")
    private Double amount;

    @TableField(value = "description")
    String description;

    @TableField(value = "type")
    String type;

}
