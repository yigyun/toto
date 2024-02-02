package com.main.toto.bookMark.repository;

import com.main.toto.auction.entity.board.Board;
import com.main.toto.auction.repository.BoardRepository;
import com.main.toto.bookMark.entity.bookMark.BookMark;
import com.main.toto.member.entity.member.Member;
import com.main.toto.member.entity.member.MemberRole;
import com.main.toto.member.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Log4j2
@ActiveProfiles("test")
@Transactional
class BookMarkRepositoryTest {

    @Autowired
    BookMarkRepository bookMarkRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @BeforeEach
    public void setup(){
        entityManager.clear();
        dataSetup();
    }

    @DisplayName("회원별 북마크 조회 및 실패 테스트")
    @Test
    void findByMember_Mid() {

        // when
        List<BookMark> foundBookMarks = bookMarkRepository.findByMember_Mid("member2");
        
        // then
        bookMarkRepository.findByMember_Mid("member1").forEach(bookMark -> {
            assertThat(bookMark.getMember().getMid()).isEqualTo("member1");
        });
        assertThat(foundBookMarks.size()).isEqualTo(0);
    }

    @DisplayName("북마크 존재 여부 확인 및 실패 테스트")
    @Test
    void existsByMemberAndBoard() {
        // given
        Member member = memberRepository.findById("member1").get();
        Board board = boardRepository.findById(1L).get();
        Board board2 = Board.builder()
                .bno(2L)
                .title("title2")
                .content("content2")
                .writer(member.getMid()).build();
        boardRepository.save(board2);

        // when & then
        assertTrue(bookMarkRepository.existsByMemberAndBoard(member, board));
        assertFalse(bookMarkRepository.existsByMemberAndBoard(member, board2));
    }

    @DisplayName("회원별 게시글 북마크 조회 및 실패 테스트")
    @Test
    void findByMemberAndBoard() {
        // given
        Member member = memberRepository.findById("member1").get();
        Board board = boardRepository.findById(1L).get();
        Board board2 = Board.builder()
                .bno(2L)
                .title("title2")
                .content("content2")
                .writer(member.getMid()).build();
        boardRepository.save(board2);

        // when & then
        assertTrue(bookMarkRepository.findByMemberAndBoard(member, board).isPresent());
        assertFalse(bookMarkRepository.findByMemberAndBoard(member, board2).isPresent());
    }

    private void dataSetup() {
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

        BookMark bookMark = BookMark.builder()
                .member(member)
                .board(board).build();
        bookMarkRepository.save(bookMark);
    }

}