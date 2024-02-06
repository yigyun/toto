package com.main.toto.auction.repository;

import com.main.toto.auction.dto.board.BoardDTO;
import com.main.toto.auction.entity.board.Board;
import com.main.toto.auction.entity.board.BoardCategory;
import com.main.toto.auction.entity.board.BoardImage;
import com.main.toto.auction.service.board.BoardService;
import com.main.toto.auction.service.board.BoardServiceImpl;
import com.main.toto.bookMark.entity.bookMark.BookMark;
import com.main.toto.bookMark.service.BookMarkService;
import com.main.toto.member.dto.member.MemberJoinDTO;
import com.main.toto.member.service.MemberService;
import com.main.toto.member.service.MemberServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
@Log4j2
class BoardRepositoryTest {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    EntityManager entityManager;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    BookMarkService bookMarkService;

    @Autowired
    BoardService boardService;

    @BeforeAll
    void setUp() {
        entityManager.clear();
        dataSetup();
    }

    @DisplayName("게시글 번호로 게시글을 조회하고, 조회한 게시글을 잠금을 걸어서 반환한다.")
    @Test
    void findWithLockByBno() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Long bno = 1L;

        Callable<Board> task1 =  () -> transactionTemplate.execute(status -> boardRepository.findWithLockByBno(bno).orElse(null));
        Callable<Board> task2 = () -> transactionTemplate.execute(status -> boardRepository.findWithLockByBno(bno).orElse(null));

        Future<Board> future1 = executorService.submit(task1);
        Future<Board> future2 = executorService.submit(task2);

        // 첫 번째 작업이 먼저 실행되고 락을 얻는다고 가정합니다.
        // 그런 다음 두 번째 작업이 실행되고 락을 얻기 위해 대기합니다.
        // 첫 번째 작업이 완료되면 두 번째 작업이 락을 얻고 실행됩니다.

        // 이를 테스트하기 위해, 두 Future가 모두 완료될 때까지 기다립니다.
        while (!(future1.isDone() && future2.isDone())) {
            log.info("대기중");
            Thread.sleep(1000);
        }

        // 두 작업이 모두 완료되면, 두 작업이 동일한 Board 객체를 반환했는지 확인합니다.
        assertEquals(future1.get(), future2.get());
    }

    @DisplayName("게시글 번호로 게시글을 조회하고, 조회한 게시글의 이미지를 함께 반환한다.")
    @Test
    void findByIdWithImages() {
        //given
        Long bno = 1L;
        //when
        Board board = boardRepository.findByIdWithImages(bno).orElse(null);
        //then
        assertNotNull(board);
        assertNotNull(board.getImageSet());
        assertEquals(1, board.getImageSet().size());
    }

    @DisplayName("게시글 카테고리별로 북마크 수가 가장 높은 게시글을 조회한다.")
    @Test
    void findTopByBoardCategoryOrderByBookMarkCountDesc() {
        //given
        long count = 14L;
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
                    .bookMarkCount(3L)
                    .boardCategory(category)
                    .fileNames(Arrays.asList(boardImage.getUuid() + "_" + boardImage.getFileName()))
                    .price(5000L)
                    .build();
            // board 객체의 필드를 설정합니다.
            boardService.register(boardDTO);
            bookMarkService.addBookMark(count - 1, "test");
        }
        //when
        Board board = boardRepository.findTopByBoardCategoryOrderByBookMarkCountDesc(BoardCategory.ETC).orElse(null);
        //then
        assertNotNull(board);
        assertEquals(BoardCategory.ETC, board.getBoardCategory());
        assertEquals(26L, board.getBno());
    }

    @DisplayName("게시글 카테고리별로 게시글을 조회한다.")
    @Test
    void findByBoardCategory() {
        //given
        long count = 14L;
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
                    .bookMarkCount(3L)
                    .boardCategory(category)
                    .fileNames(Arrays.asList(boardImage.getUuid() + "_" + boardImage.getFileName()))
                    .price(5000L)
                    .build();
            // board 객체의 필드를 설정합니다.
            boardService.register(boardDTO);
        }
        //when
        List<Board> board = boardRepository.findByBoardCategory(BoardCategory.ETC);
        //then
        assertNotNull(board);
        assertEquals(BoardCategory.ETC, board.get(0).getBoardCategory());
    }

    @DisplayName("특정 날짜 이전에 등록된 게시글을 조회한다.")
    @Test
    void findAllByRegDateBefore() {
        //given
        LocalDateTime threshold = LocalDateTime.now().minusDays(1);
        //when
        List<Board> board = boardRepository.findAllByRegDateBefore(threshold);
        //then
        assertNotNull(board);
        assertEquals(0, board.size());
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