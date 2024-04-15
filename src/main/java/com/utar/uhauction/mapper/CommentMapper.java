package com.utar.uhauction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utar.uhauction.model.entity.Comment;
import com.utar.uhauction.model.vo.CommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * getCommentsByItemID
     *
     * @param itemid
     * @return
     */
    List<CommentVO> getCommentsByItemID(@Param("itemid") String itemid);
}
