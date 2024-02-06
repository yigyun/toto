package com.main.toto.auction.controller;

import com.main.toto.auction.dto.board.BoardDTO;
import com.main.toto.auction.dto.board.BoardListAllDTO;
import com.main.toto.auction.dto.page.PageRequestDTO;
import com.main.toto.auction.dto.page.PageResponseDTO;
import com.main.toto.auction.entity.board.BoardCategory;
import com.main.toto.auction.service.board.BoardService;
import com.main.toto.bookMark.service.BookMarkService;
import com.main.toto.config.ControllerAuctionTestConfig;
import com.main.toto.global.security.dto.MemberSecurityDTO;
import com.main.toto.member.dto.member.MemberJoinDTO;
import com.main.toto.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
class BoardControllerTest  extends ControllerAuctionTestConfig{

    @SpyBean
    BoardService boardService;

    @SpyBean
    MemberService memberService;

    @SpyBean
    BookMarkService bookMarkService;

    @SpyBean
    BoardController boardController;

    @DisplayName("메인 페이지 요청시 메인 페이지를 반환한다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void favList() throws Exception {
        // given
        List<BoardDTO> dtoList = Arrays.asList(BoardDTO.builder().bno(1L).writer("test").price(23000L).title("제목").content("내용").build());
        when(boardService.favoriteMain()).thenReturn(dtoList);
        // when & then
         mvc.perform(get("/toto/main"))
                 .andExpect(status().isOk())
                 .andExpect(view().name("/toto/main"))
                 .andExpect(model().attributeExists("dtoList"));
    }


    @DisplayName("게시글 목록 요청시 게시글 목록 페이지를 반환한다.")
    @Test
    void list() throws Exception {
        //given
        // BoardListAllDTO 객체 생성
        BoardListAllDTO boardListAllDTO1 = BoardListAllDTO.builder()
                .bno(1L)
                .title("Title 1")
                .writer("Writer 1")
                .price(10000L)
                .regDate(LocalDateTime.now())
                .boardCategory(BoardCategory.BOOK)
                .boardImages(new ArrayList<>())
                .build();

        BoardListAllDTO boardListAllDTO2 = BoardListAllDTO.builder()
                .bno(2L)
                .title("Title 2")
                .writer("Writer 2")
                .price(20000L)
                .regDate(LocalDateTime.now())
                .boardCategory(BoardCategory.FASHION)
                .boardImages(new ArrayList<>())
                .build();

// BoardListAllDTO 객체를 리스트에 추가
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        List<BoardListAllDTO> dtoList = new ArrayList<>();
        int total = 100;

        PageResponseDTO<BoardListAllDTO> pageResponseDTO = PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();

        when(boardService.listWithAll(pageRequestDTO)).thenReturn(pageResponseDTO);
        //when & then
        mvc.perform(get("/toto/board/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("toto/board/list"))
                .andExpect(model().attributeExists("responseDTO"));
    }

    @DisplayName("게시글 등록 페이지 요청시 게시글 등록 페이지를 반환한다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void registerGet() throws Exception {
        mvc.perform(get("/toto/board/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("toto/board/register"));
    }

    @DisplayName("게시글 등록시 게시글 등록 후 메인 페이지로 리다이렉트한다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void registerPost() throws Exception {
        // given
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(1L)
                .title("Title")
                .content("Content")
                .writer("Writer")
                .price(10000L)
                .boardCategory(BoardCategory.BOOK)
                .build();
        when(boardService.register(boardDTO)).thenReturn(1L);
        // when & then
        mvc.perform(post("/toto/board/register")
                        .flashAttr("boardDTO", boardDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/toto/main"))
                .andExpect(flash().attributeExists("result"));
    }


    /**
     * Read 테스트 코드 작성에서 오래 걸린 이유 : html에서 타임리프로 security를 적용하는 부분이 있었는데,
     * Custom으로 만든 security User 부분이 적용이 안되서, 파싱이 안되니까 계속 400, 404 문제를 일으켰다.
     * 그래서, security를 적용하는 부분을 넣어주어서, 테스트를 진행했다.
     * @throws Exception
     */
    @DisplayName("게시글 읽기 페이지 요청시 게시글 읽기 페이지를 반환한다.")
    @Test
    @WithMockUser(username = "test", roles = "USER")
    void read() throws Exception {
        //given
        // UserDetails 객체 생성
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails principal = new MemberSecurityDTO("test", "password", "test@naver.com", false, false, authorities);

// Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);

// SecurityContext에 Authentication 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Long bno = 1L;
        BoardDTO boardDTO = BoardDTO.builder()
                .content("Content").title("Title").price(10000L).writer("test").boardCategory(BoardCategory.BOOK)
                .bno(bno).build();
        boardService.register(boardDTO);
        MemberJoinDTO memberJoinDTO = new MemberJoinDTO();
        memberJoinDTO.setMid("test");
        memberJoinDTO.setMpassword("1111");
        memberJoinDTO.setEmail("test@naver.com");
        memberService.join(memberJoinDTO);
        //when & then
        mvc.perform(get("/toto/board/read?bno=1")
                        .with(SecurityMockMvcRequestPostProcessors.user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("toto/board/read"));
    }

    @DisplayName("게시글 수정 페이지 요청시 게시글 수정 페이지를 반환한다.")
    @Test
    @WithMockUser(username = "test", roles = "USER")
    void getModify() throws Exception {
        //given
        Long bno = 1L;
        BoardDTO boardDTO = BoardDTO.builder()
                .content("Content").title("Title").price(10000L).writer("test").boardCategory(BoardCategory.BOOK)
                .bno(bno).build();
        boardService.register(boardDTO);
        MemberJoinDTO memberJoinDTO = new MemberJoinDTO();
        memberJoinDTO.setMid("test");
        memberJoinDTO.setMpassword("1111");
        memberJoinDTO.setEmail("test@naver.com");
        memberService.join(memberJoinDTO);
        //when & then
        mvc.perform(get("/toto/board/modify?bno=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("toto/board/modify"));
    }


    @DisplayName("게시글 수정시 게시글 수정 후 메인 페이지로 리다이렉트한다.")
    @Test
    @WithMockUser(username = "test", roles = "USER")
    void modify() throws Exception {
        //given
        Long bno = 1L;
        BoardDTO boardDTO = BoardDTO.builder()
                .content("Content").title("Title").price(10000L).writer("test").boardCategory(BoardCategory.BOOK)
                .bno(bno).build();
        boardService.register(boardDTO);
        MemberJoinDTO memberJoinDTO = new MemberJoinDTO();
        memberJoinDTO.setMid("test");
        memberJoinDTO.setMpassword("1111");
        memberJoinDTO.setEmail("test@naver.com");
        memberService.join(memberJoinDTO);

        boardDTO.setContent("Content2");

        //when & then
        mvc.perform(post("/toto/board/modify")
                .with(SecurityMockMvcRequestPostProcessors.user("test"))
                        .flashAttr("boardDTO", boardDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/toto/board/read?bno=1"))
                .andExpect(flash().attributeExists("result"));
    }

    @DisplayName("게시글 삭제시 게시글 삭제 후 메인 페이지로 리다이렉트한다.")
    @Test
    @WithMockUser(username = "test", roles = "USER")
    void remove() throws Exception {
        //given
        Long bno = 1L;
        BoardDTO boardDTO = BoardDTO.builder()
                .content("Content").title("Title").price(10000L).writer("test").boardCategory(BoardCategory.BOOK)
                .bno(bno).build();
        boardService.register(boardDTO);
        MemberJoinDTO memberJoinDTO = new MemberJoinDTO();
        memberJoinDTO.setMid("test");
        memberJoinDTO.setMpassword("1111");
        memberJoinDTO.setEmail("test@naver.com");
        memberService.join(memberJoinDTO);
        //when & then
        mvc.perform(post("/toto/board/remove")
                .with(SecurityMockMvcRequestPostProcessors.user("test"))
                        .flashAttr("boardDTO", boardDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/toto/main"))
                .andExpect(flash().attributeExists("result"));
    }
}