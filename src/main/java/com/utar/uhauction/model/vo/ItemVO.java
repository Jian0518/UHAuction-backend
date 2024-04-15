package com.utar.uhauction.model.vo;

import com.utar.uhauction.model.entity.Category;
import com.utar.uhauction.model.entity.Images;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemVO implements Serializable {
    private static final long serialVersionUID = -261082150965211545L;

    /**
     * item id
     */
    private String id;
    /**
     * donor id
     */
    private String donorId;
    /**
     * profile
     */
    private String avatar;
    /**
     * user alias
     */
    private String alias;

    private String username;
    /**
     * item name
     */
    private String title;
    /**
     * number of comments
     */
    private Integer comments;

    private Integer bidCount;

    private Boolean top;

    private Boolean essence;

    private Integer collects;
    /**
     * item's category
     */
    private List<Category> tags;


    private Images image;

    private String cover;

    /**
     * count of views
     */
    private Integer view;

    private Date createTime;

    private Date modifyTime;

    private Date endTime;
}
