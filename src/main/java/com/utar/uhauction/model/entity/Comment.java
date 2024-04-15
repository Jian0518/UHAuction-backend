package com.utar.uhauction.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@TableName("comment")
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * primary key
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * description
     */
    @NotBlank(message = "Description cannot be empty")
    @TableField(value = "content")
    private String content;


    /**
     * donor ID
     */
    @TableField("user_id")
    private String userId;


    @TableField("item_id")
    private String itemId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


    @TableField(value = "modify_time", fill = FieldFill.UPDATE)
    private Date modifyTime;
}
