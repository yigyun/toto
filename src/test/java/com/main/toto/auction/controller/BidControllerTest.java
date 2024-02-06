package com.main.toto.auction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.toto.auction.dto.BidDTO;
import com.main.toto.auction.dto.board.BoardDTO;
import com.main.toto.auction.service.board.BoardService;
import com.main.toto.member.dto.member.MemberJoinDTO;
import com.main.toto.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BidControllerTest {

    @Autowired
    BoardService boardService;

    @Autowired
    MemberService memberService;

    @Autowired
    BidController bidController;

    @DisplayName("입찰  요청시 입찰 페이지를 반환한다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void placeBid() throws Exception {
        // given
        BidDTO bidDTO = BidDTO.builder()
                .bno(1L).mid("test").price(23000L).build();
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(1L).writer("test").price(8000L).title("제목").content("내용").build();
        boardService.register(boardDTO);
        MemberJoinDTO memberJoinDTO = new MemberJoinDTO();
        memberJoinDTO.setMpassword("11234");
        memberJoinDTO.setMid("test");
        memberJoinDTO.setEmail("test@naver.com");
        memberService.join(memberJoinDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        String bidDTOJson = objectMapper.writeValueAsString(bidDTO);
        ResponseEntity<String> result = bidController.placeBid(bidDTO);
        assertEquals("success create", result.getBody());
    }
}