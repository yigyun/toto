package com.main.toto.bookMark.service;

import com.main.toto.auction.entity.board.Board;
import com.main.toto.auction.repository.BoardRepository;
import com.main.toto.bookMark.entity.bookMark.BookMark;
import com.main.toto.bookMark.repository.BookMarkRepository;
import com.main.toto.member.entity.member.Member;
import com.main.toto.member.entity.member.MemberRole;
import com.main.toto.member.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.awt.print.Book;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Log4j2
class BookMarkServiceImplTest {

    @SpyBean
    private BookMarkService bookMarkService;

    @SpyBean
    private BoardRepository boardRepository;

    @SpyBean
    private MemberRepository memberRepository;

    @SpyBean
    private BookMarkRepository bookMarkRepository;

    @BeforeAll
    void setUp() {
        Member member = Member.builder()
                .mid("member1")
                .mpassword("1234")
                .email("email1@aaa.com").build();
        member.addRole(MemberRole.USER);
        memberRepository.save(member);

        Board board = Board.builder()
                .bno(1L)
                .title("title1")
                .content("content1")
                .writer(member.getMid()).build();
        boardRepository.save(board);
    }

    @DisplayName("북마크 추가 테스트")
    @Test
    void addBookMark() {
        // given
        Long bno = 1L;
        String mid = "member1";
        // when
        bookMarkService.addBookMark(bno, mid);
        // then
        assertTrue(bookMarkService.existsByMemberAndBoard(mid, bno));
    }

    @DisplayName("북마크 추가 실패 테스트 - 잘못된 입력값")
    @Test
    void addBookMark_InvalidInput() {
        // given
        Long bno = -1L;
        String mid = null;
        // when & then
        assertThrows(IllegalArgumentException.class, () -> bookMarkService.addBookMark(bno, mid));
    }

    @DisplayName("북마크 추가 실패 테스트 - 이미 존재하는 북마크")
    @Test
    void addBookMark_AlreadyExists() {
        // given
        Long bno = 1L;
        String mid = "member1";
        bookMarkService.addBookMark(bno, mid); // 첫 번째 북마크 추가

        // when & then
        assertThrows(IllegalArgumentException.class, () -> bookMarkService.addBookMark(bno, mid)); // 두 번째 북마크 추가 시도
    }

    @DisplayName("북마크 리스트 조회 테스트")
    @Test
    void getBookMarkList() {
        // given
        String mid = "member1";
        bookMarkService.addBookMark(1L, mid);
        Board board = Board.builder()
                .bno(2L)
                .title("title2")
                .content("content2")
                .writer("member1").build();
        boardRepository.save(board);
        bookMarkService.addBookMark(2L, mid);
        // when
        List<BookMark> bookMarkList = bookMarkService.getBookMarkList(mid);
        // then
        assertEquals(2, bookMarkList.size());
    }

    @DisplayName("북마크 리스트 조회 잘못된 요청 테스트")
    @Test
    void getBookMarkListFail() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> bookMarkService.getBookMarkList(null));
    }

    @DisplayName("북마크 삭제 테스트")
    @Test
    void deleteBookMark() {

        //given
        Long bno = 1L;
        String mid = "member1";
        bookMarkService.addBookMark(bno, mid);
        BookMark bookMark = bookMarkService.getBookMarkList(mid).get(0);
        //when
        bookMarkService.deleteBookMark(bno, mid);
        //then
        assertFalse(bookMarkService.existsByMemberAndBoard(mid, bno));
    }

    @DisplayName("북마크 삭제 실패 테스트")
    @Test
    void deleteBookMarkFail() {
        //given
        Long bno = 1L;
        String mid = "member1";
        bookMarkService.addBookMark(bno, mid);
        //when & then
        assertThrows(EntityNotFoundException.class, () -> bookMarkService.deleteBookMark(2L, mid));
        assertThrows(IllegalArgumentException.class, () -> bookMarkService.deleteBookMark(-1L, mid));
    }

    @DisplayName("북마크 존재 여부 테스트")
    @Test
    void existsByMemberAndBoard() {
        //given
        Long bno = 1L;
        String mid = "member1";
        bookMarkService.addBookMark(bno, mid);
        //when & then
        assertTrue(bookMarkService.existsByMemberAndBoard(mid, bno));
    }

    @DisplayName("북마크 존재 여부 테스트 - 잘못된 입력값")
    @Test
    void existsByMemberAndBoardFail() {
        //when & then
        assertThrows(IllegalArgumentException.class, () -> bookMarkService.existsByMemberAndBoard(null, 1L));
        assertThrows(IllegalArgumentException.class, () -> bookMarkService.existsByMemberAndBoard("member1", -1L));
        assertThrows(EntityNotFoundException.class, () -> bookMarkService.existsByMemberAndBoard("member1", 2L));
        assertThrows(EntityNotFoundException.class, () -> bookMarkService.existsByMemberAndBoard("member2", 1L));
    }

}