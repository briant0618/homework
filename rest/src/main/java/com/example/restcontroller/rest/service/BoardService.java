package com.example.restcontroller.rest.service;

import com.example.restcontroller.rest.entity.BoardEntity;
import com.example.restcontroller.rest.repository.BoardRepository;
import com.example.restcontroller.rest.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;


    // 게시판 Write
    public void write(BoardEntity board){
        board.setDateView(0);
        boardRepository.save(board);
    }


    // 게시판 Paging + View
    public Page<BoardEntity> boardList(Pageable pageable){

        return boardRepository.findAll(pageable);
    }

    // 게시판 상세페이지 이동
    public BoardEntity boardView(Integer id){
        return boardRepository.findById(id).get();
    }


    // 게시판 삭제
    @Transactional
    public void boardDelete(Integer id) {
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다. ID: " + id));

        commentRepository.deleteByBoardEntity(board);
        System.out.println("댓글이 먼저 삭제되었습니다.");

        boardRepository.delete(board);
        System.out.println("이후에 게시물도 같이 삭제되었습니다.");
    }

    // 게시판 검색+페이징
    public Page<BoardEntity> boardSearchByName(String name, Pageable pageable) {
        return boardRepository.findByNameContainingIgnoreCase(name, pageable);
    }

}