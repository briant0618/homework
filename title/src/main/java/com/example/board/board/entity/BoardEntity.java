package com.example.board.board.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.time.LocalDateTime;
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

    @CreationTimestamp
    private LocalDateTime date;
    private Integer dateView;

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntity = new ArrayList<>();
}
