package com.main.toto.auction.controller;

import com.main.toto.auction.dto.BidDTO;
import com.main.toto.auction.dto.board.BoardDTO;
import com.main.toto.auction.dto.board.BoardListAllDTO;
import com.main.toto.auction.dto.page.PageRequestDTO;
import com.main.toto.auction.dto.page.PageResponseDTO;
import com.main.toto.auction.entity.board.BoardCategory;
import com.main.toto.auction.service.auction.AuctionServiceImpl;
import com.main.toto.auction.service.board.BoardService;
import com.main.toto.bookMark.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
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

    @PreAuthorize("isAuthenticated()")
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
    @GetMapping({ "/board/read"})
    public void read(@RequestParam("bno") Long bno, Model model, PageRequestDTO pageRequestDTO, HttpServletRequest request){
        log.info("bno: " + bno);

        HttpSession session = request.getSession();
        String referer = request.getHeader("Referer");
        session.setAttribute("prevPage", referer);

        setBoardAndBookmark(bno, model);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/board/modify"})
    public void getModify(@RequestParam("bno") Long bno, Model model, PageRequestDTO pageRequestDTO, HttpServletRequest request){
        log.info("bno: " + bno);

        extracted(bno);

        HttpSession session = request.getSession();
        String referer = request.getHeader("Referer");
        session.setAttribute("prevPage", referer);

        setBoardAndBookmark(bno, model);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/board/modify")
    public String modify(@Validated BoardDTO boardDTO,
                         PageRequestDTO pageRequestDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){
        log.info("board POST modify ....." + boardDTO);

        // 사용자의 아이디
        extracted(boardDTO.getBno());

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

    // tip: isAuthenticated만 쓰는 이유 : 테스트에서 == #board.writer 같은 것 적용이 매우 어렵.
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/board/remove")
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes){
        Long bno = boardDTO.getBno();
        log.info("remove post... " + bno);
        extracted(bno);
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

    protected void setBoardAndBookmark(Long bno, Model model) {
        BoardDTO boardDTO = boardService.readOne(bno);
        if (boardDTO == null) {
            throw new EntityNotFoundException("Board not found with bno: " + bno);
        }
        model.addAttribute("dto", boardDTO);

        // 사용자의 아이디
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mid = auth.getName();

        //북마크 확인
        boolean isBookMark = bookMarkService.existsByMemberAndBoard(mid,bno);
        model.addAttribute("isBookMark", isBookMark);
    }

    public boolean checkWriter(Long bno, String username){
        return boardService.checkWriter(bno, username);
    }

    private void extracted(Long bno) {
        // 사용자의 아이디
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            // 로그인되지 않은 사용자에 대한 처리
            throw new IllegalArgumentException("User is not authenticated");
        }
        String mid = auth.getName();

        // checkWriter 메소드를 직접 호출
        if (!checkWriter(bno, mid)) {
            throw new AccessDeniedException("Access_is_denied_getModify");
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("대상을 찾을 수 없습니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // Log the exception
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("해당 게시물이 존재하지 않습니다.");
    }
}
