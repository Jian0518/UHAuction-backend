package com.utar.uhauction.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@Accessors(chain = true)
@TableName("bid")
@NoArgsConstructor
@AllArgsConstructor
public class Bid implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * primary
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @NotBlank(message = "Amount cannot be empty")
    @TableField(value = "amount")
    private Long amount;


    /**
     * bidder id
     */
    @TableField("user_id")
    private String userId;


    @TableField("item_id")
    private String itemId;


    @TableField(value = "bid_time", fill = FieldFill.INSERT)
    private Date bidTime;
}
