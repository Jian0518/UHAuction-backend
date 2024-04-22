package com.utar.uhauction.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BidDTO implements Serializable {
    private static final long serialVersionUID = -5957433707110390518L;


    private String item_id;


    private Long amount;
}
