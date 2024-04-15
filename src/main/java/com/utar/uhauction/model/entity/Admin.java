package com.utar.uhauction.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.platform.commons.util.ToStringBuilder;


@TableName(value = "admin")
@Data
public class Admin {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String password;


}
