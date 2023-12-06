package com.main.toto.repository;

import com.main.toto.domain.board.Board;
import com.main.toto.domain.board.BoardCategory;

import com.main.toto.domain.board.BoardImage;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Log4j2
class BoardRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    BoardRepository boardRepository;


    // main 페이지에 사용할 카테고리 별로 1개씩 최고 북마크를 가져오는 메서드 테스트이다.
    @Test
    @Transactional
    void findByBoardCategoryAndBookMarkCount() {

        // 테스트 데이터 생성하기
        Board fashionBoard = Board.builder()
                .title("패션 게시글")
                .content("패션 게시글입니다.")
                .writer("패션")
                .boardCategory(BoardCategory.FASHION)
                .bookMarkCount(2L)
                .build();
        Board fashionBoard2 = Board.builder()
                .title("패션 게시글2")
                .content("패션 게시글입니다2.")
                .writer("패션2")
                .boardCategory(BoardCategory.FASHION)
                .bookMarkCount(4L)
                .build();
        Board musicBoard = Board.builder()
                .title("Music Title")
                .content("Music Content")
                .writer("Writer2")
                .bookMarkCount(3L)
                .boardCategory(BoardCategory.MUSIC)
                .build();
        Board movieBoard = Board.builder()
                .title("Movie Title")
                .content("Movie Content")
                .writer("Writer3")
                .bookMarkCount(1L)
                .boardCategory(BoardCategory.MOVIE)
                .build();

        // 저장
        boardRepository.save(fashionBoard);
        boardRepository.save(fashionBoard2);
        boardRepository.save(musicBoard);
        boardRepository.save(movieBoard);

        // 영속성 컨텍스트 날리기.
        entityManager.flush();

        // 이제 메서드 테스트
        Board check = boardRepository.findByBoardCategoryAndBookMarkCount(BoardCategory.FASHION).orElseThrow();

        assertEquals(check.getBoardCategory(), BoardCategory.FASHION);
        log.info("Title: " + check.getTitle());
        log.info("Content: " + check.getContent());
        log.info("Writer: " + check.getWriter());
        log.info("BookMarkCount: " + check.getBookMarkCount());
    }

    @Test
    public void testInsertWithImages(){

        Board board = Board.builder()
                .title("Image Test")
                .content("첨부파일 테스트")
                .writer("tester")
                .boardCategory(BoardCategory.FASHION)
                .bookMarkCount(1L)
                .build();

        for(int i = 0; i < 3; i++){
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }

        boardRepository.save(board);
    }

    // board랑 image 같이 가져오는거 확인하기.
    @Test
    @Transactional
    public void findByIdWithImages(){

        Board test = Board.builder()
                .title("Image Test")
                .content("첨부파일 테스트")
                .writer("tester")
                .boardCategory(BoardCategory.FASHION)
                .bookMarkCount(1L)
                .build();
        for(int i = 0; i < 3; i++){
            test.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }
        boardRepository.save(test);
        entityManager.flush();

        Board board = boardRepository.findById(1L).orElseThrow();

        log.info(board);
        log.info("-------------");
        for(BoardImage boardImage : board.getImageSet()){
            log.info(boardImage);
        }
    }
}