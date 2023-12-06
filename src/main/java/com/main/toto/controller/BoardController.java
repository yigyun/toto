package com.main.toto.controller;

import com.main.toto.domain.board.BoardCategory;
import com.main.toto.dto.board.BoardDTO;
import com.main.toto.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/toto")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    @Value("${com.main.upload.path}")
    private String uploadPath;

    private final BoardService boardService;

    // 후에 카테고리 별로 1개씩 가져와서 보여주는 느낌으로 할 것
    // board service에서 3개 가져옴.
    @GetMapping("/main")
    public String favList(Model model){
        // 이거 3개만 일단 가져옴.
        List<BoardDTO> dtoList = boardService.favoriteMain();
        model.addAttribute("dtoList", dtoList);
        return "/toto/main";
    }
}
