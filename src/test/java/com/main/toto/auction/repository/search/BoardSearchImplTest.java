package com.main.toto.auction.repository.search;

import com.main.toto.auction.dto.board.BoardDTO;

import com.main.toto.auction.dto.board.BoardListAllDTO;
import com.main.toto.auction.entity.board.Board;
import com.main.toto.auction.entity.board.BoardCategory;
import com.main.toto.auction.entity.board.BoardImage;
import com.main.toto.auction.entity.board.QBoard;
import com.main.toto.auction.repository.BoardRepository;
import com.main.toto.auction.service.board.BoardService;
import com.main.toto.bookMark.repository.BookMarkRepository;
import com.main.toto.bookMark.service.BookMarkService;
import com.main.toto.member.dto.member.MemberJoinDTO;
import com.main.toto.member.repository.MemberRepository;
import com.main.toto.member.service.MemberService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;

import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BoardSearchImplTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    BoardSearchImpl boardSearch;

    @Autowired
    MemberService memberService;

    @Autowired
    BookMarkService bookMarkService;

    @Autowired
    BoardService boardService;

    @BeforeEach
    public void setup() {
        dataSetup();
    }

    @DisplayName("카테고리, 키워드, 페이징을 이용한 검색 테스트")
    @Test
    void searchWithAll() {
        //given
        BoardCategory category = BoardCategory.BOOK;
        String keyword = "책";
        PageRequest pageable = PageRequest.of(0, 10);
        //when
        Page<BoardListAllDTO> result = boardSearch.searchWithAll(category, keyword, pageable);
        //then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("책 게시글", result.getContent().get(0).getTitle());
    }

    @DisplayName("키워드를 이용한 검색 테스트")
    @Test
    void searchWithKeyword() {
        //given
        String keyword = "책";
        PageRequest pageable = PageRequest.of(0, 10);
        //when
        Page<BoardListAllDTO> result = boardSearch.searchWithKeyword(keyword, pageable);
        Page<BoardListAllDTO> resultFail = boardSearch.searchWithKeyword(null, pageable);
        //then
        assertNull(resultFail);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("책 게시글", result.getContent().get(0).getTitle());
    }

    @DisplayName("카테고리를 이용한 검색 테스트")
    @Test
    void searchWithCategory() {
        //given
        BoardCategory category = BoardCategory.BOOK;
        PageRequest pageable = PageRequest.of(0, 10);
        //when
        Page<BoardListAllDTO> result = boardSearch.searchWithCategory(category, pageable);
        Page<BoardListAllDTO> resultFail = boardSearch.searchWithCategory(null, pageable);
        //then
        assertNull(resultFail);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("책 게시글", result.getContent().get(0).getTitle());
    }

    @DisplayName("북마크를 이용한 검색 테스트")
    @Test
    void searchWithBookMark() {
        //given
        bookMarkService.addBookMark(1L, "test");
        bookMarkService.addBookMark(2L, "test");
        PageRequest pageable = PageRequest.of(0, 10);
        //when
        Page<BoardListAllDTO> result = boardSearch.searchWithBookMark("test", pageable);
        //then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    private void dataSetup() {
        // 초기 Member 객체 생성
        MemberJoinDTO memberJoinDTO = new MemberJoinDTO();
        memberJoinDTO.setMid("test");
        memberJoinDTO.setMpassword("test");
        memberJoinDTO.setEmail("test@naver.com");
        // member 객체의 필드를 설정합니다.
        memberService.join(memberJoinDTO);
        MemberJoinDTO memberJoinDTO2 = new MemberJoinDTO();
        memberJoinDTO2.setMid("test2");
        memberJoinDTO2.setMpassword("test2");
        memberJoinDTO2.setEmail("test2@naver.com");
        // member 객체의 필드를 설정합니다.
        memberService.join(memberJoinDTO2);

        long count = 1L;
        // 각 카테고리별 Board 데이터 생성
        for (BoardCategory category : BoardCategory.values()) {

            BoardImage boardImage = BoardImage.builder()
                    .uuid("t" + count)
                    .fileName("t" + count + ".jpg")
                    .build();

            BoardDTO boardDTO = BoardDTO.builder()
                    .title(category.getDescription() + " 게시글")
                    .bno(count++)
                    .content(category.getDescription() + " 게시글 내용")
                    .writer("test" )
                    .bookMarkCount(1L)
                    .boardCategory(category)
                    .fileNames(Arrays.asList(boardImage.getUuid() + "_" + boardImage.getFileName()))
                    .price(5000L)
                    .build();
            // board 객체의 필드를 설정합니다.

            boardService.register(boardDTO);
        }
    }
}