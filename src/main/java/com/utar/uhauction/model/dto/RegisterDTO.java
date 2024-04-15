package com.utar.uhauction.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@Data
public class RegisterDTO {

    @NotEmpty(message = "Please enter account name")
    @Length(min = 2, max = 15, message = "Length between 2 and 15 ")
    private String name;

    @NotEmpty(message = "Please enter password")
    @Length(min = 6, max = 20, message = "Length between 6 and 20")
    private String pass;

    @NotEmpty(message = "Please enter password again")
    @Length(min = 6, max = 20, message = "Length between 6 and 20")
    private String checkPass;

    @NotEmpty(message = "Please enter email")
    @Email(message = "Format email incorrect")
    private String email;
}
