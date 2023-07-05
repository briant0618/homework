package com.example.board.board.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table( name = "ct")
@Data
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 10)
    private String name;

    @Column(length = 50)
    private String content;

    @ManyToOne
    @JoinColumn(name="boardId")
    private BoardEntity boardEntity;

    public void setBoard(BoardEntity boardEntity) {
        this.boardEntity = boardEntity;
    }
}
