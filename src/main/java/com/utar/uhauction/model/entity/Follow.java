package com.utar.uhauction.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("follow")
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * primary key
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * followed user id
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * follower id
     */
    @TableField("follower_id")
    private String followerId;

    public Follow() {
    }

}
