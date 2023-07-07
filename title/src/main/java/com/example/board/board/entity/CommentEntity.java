package com.example.board.board.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ct")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String content;
    private LocalDateTime dateSaver;

    @ManyToOne
    @JoinColumn(name="boardId")
    private BoardEntity boardEntity;

    public void setBoard(BoardEntity boardEntity) {
        this.boardEntity = boardEntity;
    }

}
