package com.utar.uhauction.controller;

import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.model.dto.CommentDTO;
import com.utar.uhauction.model.entity.Comment;
import com.utar.uhauction.model.entity.User;
import com.utar.uhauction.model.vo.CommentVO;
import com.utar.uhauction.service.ICommentService;
import com.utar.uhauction.service.IUmsUserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.utar.uhauction.jwt.JwtUtil.USER_NAME;


@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Resource
    private ICommentService bmsCommentService;
    @Resource
    private IUmsUserService umsUserService;

    @Cacheable(value = "comments", key = "#itemid", unless = "#result.data == null")
    @GetMapping("/get_comments")
    public ApiResult<List<CommentVO>> getCommentsByItemID(@RequestParam(value = "itemid", defaultValue = "1") String itemid) {
        List<CommentVO> lstBmsComment = bmsCommentService.getCommentsByItemID(itemid);
        return ApiResult.success(lstBmsComment);
    }

    @CacheEvict(value = "comments", key = "#dto.item_id")
    @PostMapping("/add_comment")
    public ApiResult<Comment> add_comment(@RequestHeader(value = USER_NAME) String userName,
                                          @RequestBody CommentDTO dto) {
        User user = umsUserService.getUserByUsername(userName);
        Comment comment = bmsCommentService.create(dto, user);
        return ApiResult.success(comment);
    }



    @Cacheable(value = "comments", key = "#id", unless = "#result.data == null")
    @GetMapping("/all")
    public ApiResult<List<CommentVO>> getItemById(@RequestParam(value = "id") String id) {
        return ApiResult.success(bmsCommentService.getCommentsByItemID(id));
    }


    @GetMapping("/delete")
    public ApiResult<String> deleteComment(@RequestParam String id){
        bmsCommentService.removeById(id);
        return ApiResult.success("Delete successfully");
    }


}
