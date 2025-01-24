package com.utar.uhauction.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("category")
@Accessors(chain = true)
public class Category implements Serializable {
    private static final long serialVersionUID = 3257790983905872243L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("name")
    private String name;
    /**
     * the number of item under the category
     */
    @TableField("item_count")
    @Builder.Default
    private Integer itemCount = 0;
}
