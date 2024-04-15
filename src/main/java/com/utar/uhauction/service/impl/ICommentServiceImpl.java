package com.utar.uhauction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utar.uhauction.mapper.CommentMapper;
import com.utar.uhauction.model.dto.CommentDTO;
import com.utar.uhauction.model.entity.Comment;
import com.utar.uhauction.model.entity.User;
import com.utar.uhauction.model.vo.CommentVO;
import com.utar.uhauction.service.ICommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class ICommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Override
    public List<CommentVO> getCommentsByItemID(String itemid) {
        List<CommentVO> lstBmsComment = new ArrayList<CommentVO>();
        try {
            lstBmsComment = this.baseMapper.getCommentsByItemID(itemid);
        } catch (Exception e) {
            log.info("lstBmsComment failed");
        }
        return lstBmsComment;
    }

    @Override
    public Comment create(CommentDTO dto, User user) {
        Comment comment = Comment.builder()
                .userId(user.getId())
                .content(dto.getContent())
                .itemId(dto.getItem_id())
                .createTime(new Date())
                .build();
        this.baseMapper.insert(comment);
        return comment;
    }
}
