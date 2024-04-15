package com.utar.uhauction.model.dto;

import lombok.Data;

@Data
public class AdminRequest {
    private Integer id;

    private String username;

    private String password;
}