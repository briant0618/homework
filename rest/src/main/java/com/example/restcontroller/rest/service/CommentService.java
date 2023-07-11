package com.example.restcontroller.rest.service;

import com.example.restcontroller.rest.entity.CommentEntity;
import com.example.restcontroller.rest.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;


    // 댓글 번호 확인하기
    public CommentEntity findCommentId(Integer commentId){
        return commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. 댓글을 다시 확인해주세요. commentId=" + commentId));
    }

    //댓글 저장
    public CommentEntity saveComment(CommentEntity commentEntity){
        commentEntity.setDateSaver(LocalDateTime.now());
        return commentRepository.save(commentEntity);

    }

    // 댓글 수정작업
    public CommentEntity updateComment(Integer commentId, CommentEntity updatedCommentEntity) {
        CommentEntity existingCommentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. 댓글을 다시 확인해주세요. commentId=" + commentId));

        existingCommentEntity.setName(updatedCommentEntity.getName());
        existingCommentEntity.setContent(updatedCommentEntity.getContent());
        return commentRepository.save(existingCommentEntity);
    }

    //댓글 삭제
    public void deleteById(Integer id) {
        commentRepository.deleteById(id);
    }

}