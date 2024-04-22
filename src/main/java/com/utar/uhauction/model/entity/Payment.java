package com.utar.uhauction.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@TableName(value = "payment")
@Data
public class Payment {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "date", fill = FieldFill.INSERT)
    private Date date;

    @TableField(value = "amount")
    private Double amount;

    @TableField(value = "user_id")
    String userId;

    @TableField(value = "item_id")
    String itemId;

    @TableField(value = "ref_id")
    String refId;

    @TableField(value = "status")
    String status;

}
