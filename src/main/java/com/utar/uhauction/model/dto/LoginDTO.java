package com.utar.uhauction.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
public class LoginDTO {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 2, max = 15, message = "Length of username between 2~15")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 20, message = "Length of password between 6~20")
    private String password;

    private Boolean rememberMe;
}
