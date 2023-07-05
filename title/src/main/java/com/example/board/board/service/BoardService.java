package com.example.board.board.service;


import com.example.board.board.entity.BoardEntity;
import com.example.board.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
public class BoardService {
   private final BoardRepository boardRepository;

    @Autowired
   public BoardService(BoardRepository boardRepository){
       this.boardRepository = boardRepository;
   }

    public Page<BoardEntity> getAllBoardsPaged(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }
    public List<BoardEntity> getAllBoards() {
        return boardRepository.findAll();
    }
    public BoardEntity getBoardById(Integer boardId){
        return boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));
    }

    public Page<BoardEntity> searchBoardPaged(String keyword, int page) {
        int pageSize = 10;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        if (keyword != null) {
            return boardRepository.findByNameContaining(keyword, pageable);
        } else {
            return boardRepository.findAll(pageable);
        }
    }
    public void insertBoard(BoardEntity boardEntity) {
        boardRepository.save(boardEntity);
    }

    public static class BoardNotFoundException extends RuntimeException {
        public BoardNotFoundException() {
            super("게시글을 찾을 수 없습니다");
        }

        public BoardNotFoundException(Integer boardId) {
            super("게시글을 찾을 수 없습니다: " + boardId);
        }
    }
    public BoardEntity updateBoard(BoardEntity board) {
        // 게시글이 데이터베이스에 존재하는지 확인합니다.
        BoardEntity existingBoard = boardRepository.findById(board.getId()).orElse(null);
        if (existingBoard == null) {

            throw new BoardNotFoundException();
        }
        existingBoard.setName(board.getName());
        existingBoard.setPrice(board.getPrice());
        existingBoard.setContent(board.getContent());

        return boardRepository.save(existingBoard);
    }

    public void deleteBoard(int id) {
        //// Optional의 value는 절대 null이 아니다.
        Optional<BoardEntity> optionalBoard = boardRepository.findById(id);
        optionalBoard.ifPresent(board -> {
            boardRepository.delete(board);
        });
    }

}
