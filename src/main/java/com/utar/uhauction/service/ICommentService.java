package com.utar.uhauction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utar.uhauction.model.dto.CommentDTO;
import com.utar.uhauction.model.entity.Comment;
import com.utar.uhauction.model.entity.User;
import com.utar.uhauction.model.vo.CommentVO;

import java.util.List;


public interface ICommentService extends IService<Comment> {

    List<CommentVO> getCommentsByItemID(String itemid);

    Comment create(CommentDTO dto, User principal);
}
