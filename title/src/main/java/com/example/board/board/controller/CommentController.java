package com.example.board.board.controller;

import com.example.board.board.entity.BoardEntity;
import com.example.board.board.entity.CommentEntity;
import com.example.board.board.service.BoardService;
import com.example.board.board.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/board/detail")
public class CommentController {
    private final BoardService boardService;
    private final CommentService commentService;

    @Autowired
    public CommentController(BoardService boardService, CommentService commentService) {
        this.boardService = boardService;
        this.commentService = commentService;
    }
    @GetMapping("/{boardId}/comments")
    public String getBoardDetail(@PathVariable("boardId") Integer boardId, Model model) {
        BoardEntity board = boardService.getBoardById(boardId);
        if (board == null) {
            throw new BoardService.BoardNotFoundException();
        }
        // 게시글 정보를 Model에 추가합니다.
        model.addAttribute("board", board);

        // 게시글에 대한 댓글 목록을 가져와서 Model에 추가합니다.
        List<CommentEntity> comments = commentService.getCommentsByBoardId(boardId);
        model.addAttribute("comments", comments);

        return "board/detail/{boardId}" ;
    }


    @PostMapping("/{boardId}/comments")
    public String createComment(@PathVariable("boardId") Integer boardId, CommentEntity comment) {
        BoardEntity board = boardService.getBoardById(boardId);
        if (board == null) {
            throw new BoardService.BoardNotFoundException();
        }
        comment.setBoardEntity(board);
        commentService.createComment(comment, boardId);

        return "redirect:/board/detail/" + boardId;
    }

}