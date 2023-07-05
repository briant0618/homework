package com.example.board.board.service;

import com.example.board.board.entity.BoardEntity;
import com.example.board.board.entity.CommentEntity;
import com.example.board.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardService boardService;


    @Autowired
    public CommentService(CommentRepository commentRepository, BoardService boardService) {
        this.commentRepository = commentRepository;
        this.boardService = boardService;
    }

    // BoardEntity의 id값 받아오기
    public List<CommentEntity> getCommentsByBoardId(Integer boardId) {
        return commentRepository.findByBoardEntityId(boardId);
    }

    // Comment 조회 및 반환
    public CommentEntity createComment(CommentEntity comment, Integer boardId) {

        BoardEntity board = boardService.getBoardById(boardId);
        comment.setBoardEntity(board);
        return commentRepository.save(comment);

    }


}