package com.utar.uhauction.model.vo;

import lombok.Data;

@Data
public class ProfileVO {


    private String id;


    private String username;


    private String alias;


    private String avatar;


    private Integer followCount;


    private Integer followerCount;


    private Integer itemCount;

    private Integer columns;


    private Integer commentCount;

}
