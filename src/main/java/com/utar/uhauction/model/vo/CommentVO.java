package com.utar.uhauction.model.vo;

import lombok.Data;

import java.util.Date;


@Data
public class CommentVO {

    private String id;

    private String content;

    private String itemId;

    private String userId;

    private String username;

    private Date createTime;

}
