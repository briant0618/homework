
package com.example.board.board.repository;


import com.example.board.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    @Override
    Page<BoardEntity> findAll(Pageable pageable);

    Page<BoardEntity> findByNameContaining(String keyword, Pageable pageable);
}