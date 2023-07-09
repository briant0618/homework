package com.example.board.board.controller;


import com.example.board.board.entity.BoardEntity;
import com.example.board.board.entity.CommentEntity;
import com.example.board.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BoardController{

    @Autowired
    private BoardService boardService;



    // write 이동
    @GetMapping("/write")
    public String writeMove(){
        return "boardwrite";
    }

    // writeform
    @PostMapping("/board/writepro")
    public String boardWrite(BoardEntity board, Model model){
        board.setDate(LocalDateTime.now());
        boardService.write(board);
        model.addAttribute("url","/list");
        return "redirect:/list";
    }


    //list 이동
    @GetMapping("/list")
    public String listMove(Model model,
                            @PageableDefault(page = 0,
                                             size = 8,
                                             sort = "id",
                                             direction = Sort.Direction.DESC)
                            Pageable pageable, String searchKeyword){
        Page<BoardEntity> list = null;
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchByName(searchKeyword, pageable);
        }

        // yyyy-mm-dd 와 hh 중간에 T가나오는 현상 수정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<String> formattedDates = list.getContent().stream()
                .map(board -> board.getDate().format(formatter))
                .collect(Collectors.toList());



        int CurrentPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(CurrentPage -1 , 1);
        int endPage = Math.min(startPage + 3, list.getTotalPages()); // 수정: endPage 제한

        boolean Previous = CurrentPage > 1;
        boolean Next = CurrentPage < list.getTotalPages() && CurrentPage < endPage; // 수정: Next 버튼 제한

        model.addAttribute("list",list);
        model.addAttribute("CurrentPage", CurrentPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("Previous", Previous);
        model.addAttribute("Next", Next);
        model.addAttribute("formattedDates", formattedDates);


        return "boardList";
    }

    // 상세페이지 보기
    @GetMapping("/boardview/{id}")
    public String viewMove(@PathVariable("id") Integer id,
                           @RequestParam(value="page", defaultValue = "0") int CurrentPage,
                           @RequestParam(value="searchKeyword", defaultValue = "") String searchKeyword,
                           Model model) {
        BoardEntity board = boardService.boardView(id);
        model.addAttribute("comments", board.getCommentEntity());  // Model에 "comments"라는 이름으로 게시글의 댓글들을 추가합니다.
        model.addAttribute("comment", new CommentEntity()); // New Comment object for the form
        model.addAttribute("board", board);
        model.addAttribute("CurrentPage", CurrentPage);
        model.addAttribute("searchKeyword", searchKeyword);
        return "/boardview";  // "/boardview"라는 이름의 뷰(view)를 반환합니다.
    }


    // 게시글 삭제
    @GetMapping("/delete")  // GET 요청을 "/delete" URL로 매핑하는 어노테이션입니다.
    public String boardDelete(Integer id){
        boardService.boardDelete(id);  // 해당 id의 게시글을 삭제합니다.
        return "redirect:/list";  // 삭제 후 "/list" URL로 리다이렉트합니다.
    }

    // 수정 게시판 이동
    @GetMapping("/modify/{id}")
    public String modifyMove(@PathVariable("id") Integer id,
                         @RequestParam(value="page", defaultValue = "0") int CurrentPage,
                         @RequestParam(value="searchKeyword", defaultValue = "") String searchKeyword,
                         Model model){
        model.addAttribute("board",boardService.boardView(id));
        model.addAttribute("CurrentPage", CurrentPage); // 현재 페이지 번호를 모델에 추가
        model.addAttribute("searchKeyword", searchKeyword); // 검색 키워드를 모델에 추가
        return "boardmodify";  // "boardmodify"라는 이름의 뷰(view)를 반환합니다.
    }

    // 수정작업
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,
                         BoardEntity board,
                         @RequestParam(value="page", defaultValue = "0") int page,
                         @RequestParam(value="searchKeyword", required = false) String searchKeyword) {

           BoardEntity boardTemp = boardService.boardView(id);
           boardTemp.setName(board.getName());
           boardTemp.setPrice(board.getPrice());
           boardTemp.setContent(board.getContent());
           boardService.write(boardTemp);
        return "redirect:/boardview/" + id + "?page=" + page + "&searchKeyword=" + searchKeyword;
    }
}