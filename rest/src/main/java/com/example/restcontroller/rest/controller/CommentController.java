package com.example.restcontroller.rest.controller;


import com.example.restcontroller.rest.entity.BoardEntity;
import com.example.restcontroller.rest.entity.CommentEntity;
import com.example.restcontroller.rest.service.BoardService;
import com.example.restcontroller.rest.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BoardService boardService;


    // 특정 댓글 select
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<CommentEntity> getComment(@PathVariable("commentId") Integer commentId) {
        CommentEntity comment = commentService.findCommentId(commentId);
        return ResponseEntity.ok(comment);
    }

    // 댓글 Insert
    @PostMapping("/boardview/{boardId}/addComment")
    public ResponseEntity<CommentEntity> addComment(@RequestBody CommentEntity commentEntity,
                                                    @PathVariable("boardId") Integer boardId) {

        BoardEntity boardEntity = boardService.boardView(boardId);
        commentEntity.setBoard(boardEntity);
        commentService.saveComment(commentEntity);

        return ResponseEntity.ok(commentEntity);
    }


    // 댓글 save+update 작업
    @PutMapping("/boardview/{boardId}/updateComment/{commentId}")
    public ResponseEntity<CommentEntity> updateComment(@RequestBody CommentEntity updatedCommentEntity,
                                                       @PathVariable("commentId") Integer commentId) {

        //Comment Id로 지정된 Comment Select
        CommentEntity existingCommentEntity = commentService.updateComment(commentId,updatedCommentEntity);
        if (existingCommentEntity == null) {
            return ResponseEntity.notFound().build();
        }

        //Comment Update
        existingCommentEntity.setName(updatedCommentEntity.getName());
        existingCommentEntity.setContent(updatedCommentEntity.getContent());

        //Update된 Comment 저장
        commentService.saveComment(existingCommentEntity);

        return ResponseEntity.ok(existingCommentEntity);
    }

    // 댓글 삭제
    @DeleteMapping("/boardview/{boardId}/deleteComment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Integer commentId){
        CommentEntity commentEntity = commentService.findCommentId(commentId);
        if (commentEntity == null) {
            return ResponseEntity.notFound().build();
        }

        commentService.deleteById(commentId);

        return ResponseEntity.ok("Delete Success");
    }
}