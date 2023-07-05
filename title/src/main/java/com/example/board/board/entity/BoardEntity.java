package com.example.board.board.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table( name = "bt")
@Data
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private int price;

    private String content;

    private String date;

    @OneToMany(mappedBy = "boardEntity")
    private List<CommentEntity> commentEntity = new ArrayList<>();
}
