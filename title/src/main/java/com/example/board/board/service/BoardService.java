package com.example.board.board.service;


import com.example.board.board.entity.BoardEntity;
import com.example.board.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    public void write(BoardEntity board){
        board.setDateView(0);
        boardRepository.save(board);
    }

    public Page<BoardEntity> boardList(Pageable pageable){

            return boardRepository.findAll(pageable);
    }
    public BoardEntity boardView(Integer id){
        return boardRepository.findById(id).get();
    }

    public void boardDelete(Integer id){
        boardRepository.deleteById(id);
    }

    public Page<BoardEntity> boardSearchByName(String name, Pageable pageable) {
        return boardRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}