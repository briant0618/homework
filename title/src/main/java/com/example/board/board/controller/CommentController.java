package com.example.board.board.controller;


import com.example.board.board.entity.BoardEntity;
import com.example.board.board.entity.CommentEntity;
import com.example.board.board.service.BoardService;
import com.example.board.board.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;



@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BoardService boardService;

    //댓글 등록
    @PostMapping("/boardview/{boardId}")
    public String addComment(@ModelAttribute CommentEntity commentEntity,
                             @PathVariable("boardId") Integer boardId,
                             @RequestParam(value="page", defaultValue = "0") int page,
                             @RequestParam(value="searchKeyword", required = false) String searchKeyword) {
        BoardEntity boardEntity = boardService.boardView(boardId);
        commentEntity.setBoard(boardEntity);
        commentService.saveComment(commentEntity);
        String encodedSearchKeyword = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8);
        System.out.println("댓글이 작성 되었습니다.");
        return "redirect:/boardview/" + boardId + "?page=" + page + "&searchKeyword=" + encodedSearchKeyword;
    }


    // 댓글 삭제 기능
    @PostMapping("/deleteComment/{commentId}")
    public String deleteComment(@PathVariable("commentId") Integer commentId,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
        CommentEntity commentEntity = commentService.boardIdFinder(commentId);
        Integer boardId = commentEntity.getBoardEntity().getId();
        commentService.deleteById(commentId);
        String encodedSearchKeyword = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8);
        System.out.println("댓글이 삭제 되었습니다.");
        return "redirect:/boardview/" + boardId + "?page=" + page + "&searchKeyword=" + encodedSearchKeyword;
    }

}

