package com.main.toto.controller;

import com.main.toto.domain.board.BoardCategory;
import com.main.toto.dto.board.BoardDTO;
import com.main.toto.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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

//    @PreAuthorize("hasRole('USER')")
    @GetMapping("/board/register")
    public void registerGet(){}

    @ModelAttribute("boardCategories")
    public BoardCategory[] boardCategories(){
        return BoardCategory.values();
    }

    @PostMapping("/board/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("board POST register .....");

        if(bindingResult.hasErrors()){
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/toto/board/register";
        }

        log.info(boardDTO);

        Long bno = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("result" , bno);

        return "redirect:/toto/main";
    }


}
