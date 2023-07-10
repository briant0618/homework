package com.example.board.board.service;


import com.example.board.board.entity.BoardEntity;
import com.example.board.board.repository.BoardRepository;
import com.example.board.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;


    // 게시판 Write
    public void write(BoardEntity board){
        board.setDateView(0);
        boardRepository.save(board);
    }


    // 게시판 Paging + View
    public Page<BoardEntity> boardList(Pageable pageable){

            return boardRepository.findAll(pageable);
    }

    // 게시판 수정페이지 이동
    public BoardEntity boardView(Integer id){
        return boardRepository.findById(id).get();
    }


    // 게시판 삭제
    public void boardDelete(Integer id){
        boardRepository.deleteById(id);
    }

    // 게시판 검색+페이징
    public Page<BoardEntity> boardSearchByName(String name, Pageable pageable) {
        return boardRepository.findByNameContainingIgnoreCase(name, pageable);
    }

}