package com.example.board.board.repository;

import com.example.board.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;





public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {


}
