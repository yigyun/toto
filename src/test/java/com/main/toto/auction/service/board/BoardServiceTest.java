package com.main.toto.auction.service.board;

import com.main.toto.auction.dto.board.BoardDTO;
import com.main.toto.auction.dto.board.BoardListAllDTO;
import com.main.toto.auction.dto.page.PageRequestDTO;
import com.main.toto.auction.dto.page.PageResponseDTO;
import com.main.toto.auction.entity.board.BoardCategory;
import com.main.toto.auction.entity.board.BoardImage;
import com.main.toto.bookMark.service.BookMarkService;
import com.main.toto.member.dto.member.MemberJoinDTO;
import com.main.toto.member.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Log4j2
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BookMarkService bookMarkService;

    @DisplayName("게시글 등록 테스트")
    @Test
    void register() {
        //given
        long count = 1L;
        //when & then
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

    @DisplayName("게시글 등록 테스트")
    @Test
    void registerFail() {
        assertThrows(IllegalArgumentException.class, () -> {
            boardService.register(null);
        });
    }

    @DisplayName("게시글 조회 테스트")
    @Test
    void readOne() {
        //given
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(1L)
                .title("test")
                .content("test")
                .writer("test")
                .bookMarkCount(1L)
                .boardCategory(BoardCategory.BOOK)
                .price(5000L)
                .build();
        boardService.register(boardDTO);
        //when
        BoardDTO result = boardService.readOne(1L);
        //then
        assertEquals(boardDTO.getBno(), result.getBno());
    }

    @DisplayName("게시글 조회 실패 테스트")
    @Test
    void readOneFail() {
        assertThrows(EntityNotFoundException.class, () -> {
            boardService.readOne(1L);
        });
    }

    @DisplayName("게시글 날짜 조회 테스트")
    @Test
    void readDate() {
        //given
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(1L)
                .title("test")
                .content("test")
                .writer("test")
                .bookMarkCount(1L)
                .boardCategory(BoardCategory.BOOK)
                .price(5000L)
                .build();
        boardService.register(boardDTO);
        //when
        BoardDTO result = boardService.readOne(1L);
        //then
        assertNotNull(result.getRegDate());
    }

    @DisplayName("게시글 수정 테스트")
    @Test
    void modify() {
        //given
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(1L)
                .title("test")
                .content("test")
                .writer("test")
                .bookMarkCount(1L)
                .boardCategory(BoardCategory.BOOK)
                .price(5000L)
                .build();
        boardService.register(boardDTO);
        //when
        boardDTO.setTitle("test2");
        boardService.modify(boardDTO);
        //then
        assertEquals("test2", boardService.readOne(1L).getTitle());
    }

    @DisplayName("게시글 수정 실패 테스트")
    @Test
    void modifyFail() {
        //given
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(1L)
                .title("test")
                .content("test")
                .writer("test")
                .bookMarkCount(1L)
                .boardCategory(BoardCategory.BOOK)
                .price(5000L)
                .build();
        boardService.register(boardDTO);
        //when & then
        boardDTO.setTitle(" ");
        assertThrows(IllegalArgumentException.class, () -> {
            boardService.modify(boardDTO);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            boardService.modify(null);
        });
        boardDTO.setBno(100L);
        boardDTO.setTitle("test2");
        assertThrows(EntityNotFoundException.class, () -> {
            boardService.modify(boardDTO);
        });
    }

    @DisplayName("게시글 삭제 및 삭제실패 테스트")
    @Test
    void remove() {
        //given
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(1L)
                .title("test")
                .content("test")
                .writer("test")
                .bookMarkCount(1L)
                .boardCategory(BoardCategory.BOOK)
                .price(5000L)
                .build();
        boardService.register(boardDTO);
        //when & then
        boardService.remove(1L);
        assertThrows(EntityNotFoundException.class, () -> {
            boardService.readOne(100L);
        });
    }

    @DisplayName("메인 화면 조회 테스트")
    @Test
    void favoriteMain() {
        //given
        dataSetup();
        //when
        //then
        assertNotNull(boardService.favoriteMain());
    }

    @DisplayName("게시글 목록 조회 테스트")
    @Test
    void listWithAll() {
        //given
        dataSetup();
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .keyword("게시글")
                .boardCategory(BoardCategory.BOOK)
                .page(1)
                .size(10)
                .build();
        //when
        PageResponseDTO<BoardListAllDTO> result1 = boardService.listWithAll(pageRequestDTO);
        //then
        assertNotNull(result1);
        assertFalse(result1.getDtoList().isEmpty());
        result1.getDtoList().forEach(boardListAllDTO -> {
            assertEquals("책 게시글", boardListAllDTO.getTitle());
        });
        pageRequestDTO.setKeyword(" ");
        PageResponseDTO<BoardListAllDTO> result2 = boardService.listWithAll(pageRequestDTO);
        assertNotNull(result2);
        result2.getDtoList().forEach(boardListAllDTO -> {
            assertEquals("책 게시글", boardListAllDTO.getTitle());
        });
        pageRequestDTO.setBoardCategory(null);
        pageRequestDTO.setKeyword("책");
        PageResponseDTO<BoardListAllDTO> result3 = boardService.listWithAll(pageRequestDTO);
        result2.getDtoList().forEach(boardListAllDTO -> {
            assertEquals("책 게시글", boardListAllDTO.getTitle());
        });
    }

    @DisplayName("북마크를 이용한 검색 테스트")
    @Test
    void listWithBookMark() {
        //given
        dataSetup();
        bookMarkService.addBookMark(1L, "test");
        //when
        PageResponseDTO<BoardListAllDTO> result = boardService.listWithBookMark(
                PageRequestDTO.builder()
                        .keyword("게시글")
                        .boardCategory(BoardCategory.BOOK).page(1).size(10).build(), "test");
        //then
        assertNotNull(result);
        assertFalse(result.getDtoList().isEmpty());
    }

    @DisplayName("작성자 확인 테스트")
    @Test
    void checkWriter() {
        //given
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(1L)
                .title("test")
                .content("test")
                .writer("test")
                .bookMarkCount(1L)
                .boardCategory(BoardCategory.BOOK)
                .price(5000L)
                .build();
        boardService.register(boardDTO);
        //when
        boolean result = boardService.checkWriter(1L, "test");
        //then
        assertTrue(result);
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