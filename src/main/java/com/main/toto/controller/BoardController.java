package com.main.toto.controller;

import com.main.toto.domain.board.BoardCategory;
import com.main.toto.dto.BidDTO;
import com.main.toto.dto.board.BoardDTO;
import com.main.toto.dto.board.BoardListAllDTO;
import com.main.toto.dto.page.PageRequestDTO;
import com.main.toto.dto.page.PageResponseDTO;
import com.main.toto.service.AuctionServiceImpl;
import com.main.toto.service.BoardService;
import com.main.toto.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping("/toto")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    @Value("${com.main.upload.path}")
    private String uploadPath;

    private final BoardService boardService;
    private final BookMarkService bookMarkService;
    private final AuctionServiceImpl auctionService;

    //  카테고리 별로 1개씩 가져와서 보여주는 느낌으로 할 것
    @GetMapping("/main")
    public String favList(PageRequestDTO  pageRequestDTO, Model model){

        log.info("main GET....");
        List<BoardDTO> dtoList = boardService.favoriteMain();
        model.addAttribute("dtoList", dtoList);
        return "/toto/main";
    }

    @PostMapping("/board/bid")
    public ResponseEntity<String> placeBid(@RequestBody BidDTO bidDTO){
        log.info("bid Post... : " + bidDTO);

        if(auctionService.getBid(bidDTO.getBno()) != null){
            auctionService.updateBid(bidDTO);
            return ResponseEntity.ok("success update");
        }else{
            auctionService.createBid(bidDTO);
            return ResponseEntity.ok("success create");
        }
    }

    @GetMapping("/board/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        log.info("board list GET....");
        PageResponseDTO<BoardListAllDTO> responseDTO =
                boardService.listWithAll(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
    }

    @PreAuthorize("hasRole('USER')")
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/board/modify", "/board/read"})
    public void read(@RequestParam Long bno, Model model, PageRequestDTO pageRequestDTO, HttpServletRequest request){
        log.info("bno: " + bno);

        HttpSession session = request.getSession();
        String referer = request.getHeader("Referer");
        session.setAttribute("prevPage", referer);

        BoardDTO boardDTO = boardService.readOne(bno);
        model.addAttribute("dto", boardDTO);

        // 사용자의 아이디
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mid = auth.getName();

        //북마크 확인
        boolean isBookMark = bookMarkService.existsByMemberAndBoard(mid,bno);
        model.addAttribute("isBookMark", isBookMark);
    }

    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/board/modify")
    public String modify(@Validated BoardDTO boardDTO,
                         PageRequestDTO pageRequestDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){
        log.info("board POST modify ....." + boardDTO);

        if(bindingResult.hasErrors()){
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            String link = pageRequestDTO.getLink();

            return "redirect:/toto/board/modify?" + link;
        }

        boardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("result" , "modified");

        redirectAttributes.addAttribute("bno", boardDTO.getBno());


        return "redirect:/toto/board/read";
    }

    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/board/remove")
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes){

        Long bno = boardDTO.getBno();
        log.info("remove post... " + bno);

        boardService.remove(bno);

        log.info(boardDTO.getFileNames());
        List<String> fileNames = boardDTO.getFileNames();
        if(fileNames != null && fileNames.size() > 0){
            removeFiles(fileNames);
        }

        redirectAttributes.addFlashAttribute("result", "removed");
        // 후에 이동할 곳 다시 입력
        log.info("remove 성공");

        return "redirect:/toto/main";
    }

    public void removeFiles(List<String> files) {

        for(String fileName : files){

            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
            String resourceName = resource.getFilename();
            try{
                String contentType = Files.probeContentType(resource.getFile().toPath());

                if(!resource.getFile().delete())
                    log.error("delete file error");



                if(contentType.startsWith("image")){
                    File thumbnail = new File(uploadPath + File.separator + "s_" + fileName);
                    if(!thumbnail.delete())
                        log.error("delete file error");
                }
            } catch (Exception e){
                log.error("delete file error - " + e.getMessage());
            }
        }
    }
}
