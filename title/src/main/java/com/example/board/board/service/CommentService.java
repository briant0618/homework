package com.example.board.board.service;

import com.example.board.board.entity.CommentEntity;
import com.example.board.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;


    public CommentEntity boardIdFinder(Integer boardId){
        return commentRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. boardId=" + boardId));
    }

    //
    public CommentEntity saveComment(CommentEntity commentEntity){
        commentEntity.setDateSaver(LocalDateTime.now());
        return commentRepository.save(commentEntity);
    }
    //댓글 삭제
    public void deleteById(Integer boardId) {
        commentRepository.deleteById(boardId);
    }

}