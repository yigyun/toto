package com.main.toto.auction.repository;

import com.main.toto.auction.dto.BidDTO;
import com.main.toto.auction.dto.board.BoardDTO;
import com.main.toto.auction.entity.bid.Bid;
import com.main.toto.auction.entity.board.Board;
import com.main.toto.auction.entity.board.BoardCategory;
import com.main.toto.auction.entity.board.BoardImage;
import com.main.toto.auction.service.auction.AuctionService;
import com.main.toto.auction.service.auction.AuctionServiceImpl;
import com.main.toto.auction.service.board.BoardService;
import com.main.toto.member.dto.member.MemberJoinDTO;
import com.main.toto.member.service.MemberService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;

import java.util.Arrays;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BidRepositoryTest {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberService memberService;

    @Autowired
    BoardService boardService;

    @Autowired
    AuctionService auctionService;

    @Autowired
    BidRepository bidRepository;

    @BeforeAll
    void setUp() {
        entityManager.clear();
        dataSetup();
    }

    @DisplayName("Bid 엔티티를 Member와 Board로 조회하는 테스트")
    @Test
    void findByMember_MidAndBoard_Bno() {
        //given
        Long bno = 1L;
        String mid = "test";
        auctionService.createBid(new BidDTO(1L, bno, mid, 10000L));
        //when
        Bid bid = bidRepository.findByMember_MidAndBoard_Bno(mid, bno).orElse(null);
        //then
        assertNotNull(bid);
        assertEquals(bid.getBoard().getBno(), bno);
    }

    @DisplayName("Bid 엔티티를 Member와 Board로 Lock 걸어서 조회하는 테스트")
    @Test
    void findWithLockByMember_MidAndBoard_Bno() throws ExecutionException, InterruptedException {
        //given
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Long bno = 1L;
        String mid = "test";
        auctionService.createBid(new BidDTO(1L, bno, mid, 10000L));
        auctionService.createBid(new BidDTO(2L, bno, "test2", 11000L));
        //when
        Callable<Bid> task1 =  () -> transactionTemplate.execute(status -> bidRepository.findWithLockByMember_MidAndBoard_Bno(mid, bno).orElse(null));
        Callable<Bid> task2 =  () -> transactionTemplate.execute(status -> bidRepository.findWithLockByMember_MidAndBoard_Bno(mid, bno).orElse(null));
        Future<Bid> future1 = executorService.submit(task1);
        Future<Bid> future2 = executorService.submit(task2);
        while (!(future1.isDone() && future2.isDone())) {
            Thread.sleep(1000);
        }
        Bid bid = bidRepository.findWithLockByMember_MidAndBoard_Bno("test2", bno).orElse(null);
        //then
        assertEquals(future1.get(), future2.get());
        assertNotNull(bid);
        assertEquals(bid.getBoard().getBno(), bno);
        assertEquals(bid.getMember().getMid(), "test2");
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