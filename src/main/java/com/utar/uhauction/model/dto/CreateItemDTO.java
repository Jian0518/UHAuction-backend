package com.utar.uhauction.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CreateItemDTO implements Serializable {
    private static final long serialVersionUID = -5957433707110390852L;

    private String title;

    private String content;

    private List<String> tags;

    private List<String> images;

    private String cover;

    private Date endTime;

}
