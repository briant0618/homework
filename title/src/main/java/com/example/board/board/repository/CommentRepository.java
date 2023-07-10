package com.example.board.board.repository;

import com.example.board.board.entity.BoardEntity;
import com.example.board.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;





public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    // Transcation 처리를 위해 BoardEntity의 id를 CommentService에 전달
    void deleteByBoardEntity(BoardEntity boardEntity);
}
