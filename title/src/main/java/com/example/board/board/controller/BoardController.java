package com.example.board.board.controller;

import com.example.board.board.entity.BoardEntity;
import com.example.board.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // Paging Mapping
    @GetMapping("/")
    public String index(Model model, @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                        @RequestParam(value = "search", required = false) String search){
        try {
            int pageSize = 10;
            if (page == null || page <= 0) {
                page = 1;
            }

            Pageable pageable = PageRequest.of(page - 1, pageSize);
            Page<BoardEntity> boardPage = boardService.getAllBoardsPaged(pageable);
            List<BoardEntity> boards = boardPage.getContent();
            int totalPages = boardPage.getTotalPages();

            model.addAttribute("boards", boards);
            int displayPageCount = 5;
            int currentPage = page; // 현재 페이지
            int startPage = Math.max(1, currentPage - (displayPageCount / 2));
            int endPage = Math.min(startPage + displayPageCount - 1, totalPages);

            model.addAttribute("totalPages", totalPages != 0 ? totalPages : 1);
            model.addAttribute("currentPage", page);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);

            return "index";

        } catch (Exception e) {
            // 예외 처리 로직 추가
            model.addAttribute("errorMessage", "An error occurred while loading the board list.");
            return "error"; // error.html로 리다이렉트 또는 에러 페이지를 반환하도록 수정해야 할 수 있습니다.
        }
    }

    // Search Paging Mapping
    @GetMapping("/search")
    public String searchList(
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {
        try {
            // 검색 키워드와 페이징 정보를 이용하여 검색 결과를 가져옴
            Page<BoardEntity> boardPage = searchKeyword != null ? boardService.searchBoardPaged(searchKeyword, page) : boardService.searchBoardPaged(null, page);
            List<BoardEntity> searchedBoards = boardPage.getContent();
            int totalPages = boardPage.getTotalPages();

            // 페이지 버튼 표시를 위한 시작 페이지와 끝 페이지 계산
            int displayPageCount = 5;
            int startPage = Math.max(1, page - (displayPageCount / 2));
            int endPage = Math.min(startPage + displayPageCount - 1, totalPages);

            model.addAttribute("boards", searchedBoards);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("searchKeyword", searchKeyword);
            model.addAttribute("search", search);

            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);

            return "index";
        } catch (Exception e) {
            // 예외 처리 로직 추가
            model.addAttribute("errorMessage", "An error occurred while searching the board list.");
            return "error"; // error.html로 리다이렉트 또는 에러 페이지를 반환하도록 수정해야 할 수 있습니다.
        }
    }

    // Detail Page Mapping
    @GetMapping("/board/detail/{id}")
    public String BoardDetail(@PathVariable("id") int id, Model model) {
        try {
            List<BoardEntity> boards = boardService.getAllBoards();

            // id에 해당하는 BoardEntity 객체 찾기
            BoardEntity board = boards.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
            if (board != null) {
                model.addAttribute("board", board);
                return "/board/detail";
            } else {
                // 예외 처리 로직 추가
                model.addAttribute("errorMessage", "The requested board does not exist.");
                return "error"; // error.html로 리다이렉트 또는 에러 페이지를 반환하도록 수정해야 할 수 있습니다.
            }
        } catch (Exception e) {
            // 예외 처리 로직 추가
            model.addAttribute("errorMessage", "An error occurred while loading the board detail.");
            return "error"; // error.html로 리다이렉트 또는 에러 페이지를 반환하도록 수정해야 할 수 있습니다.
        }
    }

    // Insert
    @GetMapping("/board/regist")
    public String Regist() {
        return "board/regist";
    }

    @PostMapping("/submit-form")
    public String submitForm(@ModelAttribute BoardEntity boardEntity, Model model) {
        try {
            String dateTime = LocalDateTime.now().toString(); // 현재 시간을 문자열로 가져옴
            boardEntity.setDate(dateTime);
            boardService.insertBoard(boardEntity);
            return "redirect:/";
        } catch (Exception e) {
            // 예외 처리 로직 추가
            model.addAttribute("errorMessage", "An error occurred while submitting the board.");
            return "error"; // error.html로 리다이렉트 또는 에러 페이지를 반환하도록 수정해야 할 수 있습니다.
        }
    }

    // Update
    @GetMapping("/board/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        try {
            // 게시글 ID를 통해 해당 게시글을 가져옵니다.
            List<BoardEntity> boards = boardService.getAllBoards();
            BoardEntity board = boards.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
            if (board != null) {
                model.addAttribute("board", board);
                return "board/update";
            } else {
                // 예외 처리 로직 추가
                model.addAttribute("errorMessage", "The requested board does not exist.");
                return "error"; // error.html로 리다이렉트 또는 에러 페이지를 반환하도록 수정해야 할 수 있습니다.
            }
        } catch (Exception e) {
            // 예외 처리 로직 추가
            model.addAttribute("errorMessage", "An error occurred while loading the board update form.");
            return "error"; // error.html로 리다이렉트 또는 에러 페이지를 반환하도록 수정해야 할 수 있습니다.
        }
    }

    @PostMapping("/board/update/{id}")
    public String updateBoard(@PathVariable("id") int id, @ModelAttribute BoardEntity updatedBoard, Model model) {
        try {
            List<BoardEntity> boards = boardService.getAllBoards();
            BoardEntity existingBoard = boards.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
            if (existingBoard != null) {
                existingBoard.setName(updatedBoard.getName());
                existingBoard.setPrice(updatedBoard.getPrice());
                existingBoard.setContent(updatedBoard.getContent());

                boardService.updateBoard(existingBoard);
                return "redirect:/board/detail/" + id;
            } else {
                // 예외 처리 로직 추가
                model.addAttribute("errorMessage", "The requested board does not exist.");
                return "error"; // error.html로 리다이렉트 또는 에러 페이지를 반환하도록 수정해야 할 수 있습니다.
            }
        } catch (Exception e) {
            // 예외 처리 로직 추가
            model.addAttribute("errorMessage", "An error occurred while updating the board.");
            return "error"; // error.html로 리다이렉트 또는 에러 페이지를 반환하도록 수정해야 할 수 있습니다.
        }
    }

    // Delete
    @PostMapping("/board/detail/{id}")
    public String deleteBoard(@PathVariable("id") int id, Model model) {
        try {
            boardService.deleteBoard(id);
            return "redirect:/";
        } catch (Exception e) {
            // 예외 처리 로직 추가
            model.addAttribute("errorMessage", "An error occurred while deleting the board.");
            return "error"; // error.html로 리다이렉트 또는 에러 페이지를 반환하도록 수정해야 할 수 있습니다.
        }
    }
}
