package com.utar.uhauction.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class BidVO {
    private String id;

    private Long amount;

    private String itemId;

    private String userId;

    private String username;

    private Date bidTime;
}
