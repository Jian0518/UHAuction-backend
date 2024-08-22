package com.utar.uhauction.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Builder
@TableName("item")
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * primary key
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @NotBlank(message = "Item name cannot be empty")
    @TableField(value = "title")
    private String title;
    /**
     * description
     */
    @NotBlank(message = "Description cannot be empty")
    @TableField("`content`")
    private String content;


    @TableField("donor_id")
    private String donorId;

    @TableField("winner_id")
    private String winnerId;

    @TableField("comments")
    @Builder.Default
    private Integer comments = 0;

    @TableField("collects")
    @Builder.Default
    private Integer collects = 0;


    @TableField("view")
    @Builder.Default
    private Integer view = 0;

    @TableField("bid_count")
    @Builder.Default
    private Integer bidCount = 0;


    @TableField("section_id")
    @Builder.Default
    private Integer sectionId = 0;


    @TableField("top")
    @Builder.Default
    private Boolean top = false;


    @TableField("essence")
    @Builder.Default
    private Boolean essence = false;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


    @TableField(value = "modify_time", fill = FieldFill.UPDATE)
    private Date modifyTime;

    @TableField(value = "end_time")
    private Date endTime;

    @TableField(value = "highest_bid")
    private Long highestBid = 0L;

    @TableField(value = "cover")
    private String cover;

    @TableField(value="is_end")
    private Integer isEnd = 0;

    @TableField(value="pay_link")
    private String payLink;

    @TableField(value = "is_pay")
    private Integer isPay = 0;

    @TableField(value="address")
    private String address = null;

    @TableField(value="is_notify")
    private Integer isNotify;
}
