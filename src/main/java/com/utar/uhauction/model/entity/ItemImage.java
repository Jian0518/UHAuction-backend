package com.utar.uhauction.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("item_image")
@Accessors(chain = true)
public class ItemImage implements Serializable {

    private static final long serialVersionUID = -5028599844989220518L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("img_id")
    private String imgId;

    @TableField("item_id")
    private String itemId;
}
