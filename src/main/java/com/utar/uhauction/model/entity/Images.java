package com.utar.uhauction.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("images")
@Accessors(chain = true)
public class Images implements Serializable {
    private static final long serialVersionUID = 3257790983905870518L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("name")
    private String name;
}
