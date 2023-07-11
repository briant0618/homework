package com.example.restcontroller.rest.controller;

import com.example.restcontroller.rest.entity.BoardEntity;
import com.example.restcontroller.rest.entity.CommentEntity;
import com.example.restcontroller.rest.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;



@RestController
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시글 쓰기 페이지 이동
    @GetMapping("/write")
    public String writeMove() {
        return "boardwrite";
    }

    // 게시글 쓰기
    @PostMapping("/board/writepro")
    // write에서 BoardEntity의 list를 확인하여 내가 등록한 값 보이도록
    public ResponseEntity<BoardEntity> boardWrite(@RequestBody BoardEntity board) {
        board.setDate(LocalDateTime.now());
        boardService.write(board);
        return ResponseEntity.ok(board);
    }

    // 게시글리스트 이동
    @GetMapping("/list")
    public ResponseEntity<?> listMove(
            @PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String searchKeyword) {
        Page<BoardEntity> list;
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchByName(searchKeyword, pageable);
        }

        // yyyy-mm-dd 와 hh 중간에 T가나오는 현상 수정
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        List<String> formattedDates = list.getContent().stream()
//                .map(board -> board.getDate().format(formatter))
//                .collect(Collectors.toList());

        int CurrentPage = list.getPageable().getPageNumber() + 1;

        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        response.put("CurrentPage", CurrentPage);
//        response.put("formattedDates", formattedDates);
        response.put("searchKeyword",searchKeyword);

        return ResponseEntity.ok(response);
    }

    // 수정 게시판 이동
    @GetMapping("/modify/{id}")
    public ResponseEntity<BoardEntity> modifyMove(@PathVariable("id") Integer id) {
        BoardEntity board = boardService.boardView(id);
        return ResponseEntity.ok(board);
    }

    // 수정작업
    @PutMapping("/update/{id}")
    public BoardEntity update(@PathVariable("id") Integer id, @RequestBody BoardEntity updatedBoard) {
        BoardEntity board = boardService.boardView(id);
        board.setName(updatedBoard.getName());
        board.setPrice(updatedBoard.getPrice());
        board.setContent(updatedBoard.getContent());
        boardService.write(board);

        // 수정된 BoardEntity 객체를 반환
        return board;
    }

    // 상세 페이지 보기
    @GetMapping("/boardview/{id}")
    @ResponseBody
    public ResponseEntity<BoardEntity> viewMove(@PathVariable("id") Integer id,
                           @RequestParam(value="page", defaultValue = "0") int CurrentPage,
                           @RequestParam(value="searchKeyword", required = false) String searchKeyword,
                           Model model) {
        BoardEntity board = boardService.boardView(id);
        model.addAttribute("comments", board.getCommentEntity());
        model.addAttribute("comment", new CommentEntity());
        model.addAttribute("board", board);
        model.addAttribute("CurrentPage", CurrentPage);
        model.addAttribute("searchKeyword", searchKeyword);
        return ResponseEntity.ok(board);
    }
    // 삭제 작업
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> boardDelete(@PathVariable("id") Integer id) {
        boardService.boardDelete(id);
        return ResponseEntity.ok("Delete Success");
    }

}

