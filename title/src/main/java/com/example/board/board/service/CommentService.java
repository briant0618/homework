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


    // Board의 id를 찾아서 댓글이 어딧는지 찾기
    public CommentEntity boardIdFinder(Integer boardId){
        return commentRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. boardId=" + boardId));
    }

    //댓글 저장
    public CommentEntity saveComment(CommentEntity commentEntity){
        commentEntity.setDateSaver(LocalDateTime.now());
        return commentRepository.save(commentEntity);
    }
    //댓글 삭제
    public void deleteById(Integer id) {
        commentRepository.deleteById(id);
    }

}