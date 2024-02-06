package com.main.toto.auction.service.auction;

import com.main.toto.auction.dto.BidDTO;
import com.main.toto.auction.dto.board.BoardDTO;
import com.main.toto.auction.entity.board.BoardCategory;
import com.main.toto.auction.entity.board.BoardImage;
import com.main.toto.auction.repository.BidRepository;
import com.main.toto.auction.repository.BoardRepository;
import com.main.toto.auction.service.board.BoardService;
import com.main.toto.member.dto.member.MemberJoinDTO;
import com.main.toto.member.repository.MemberRepository;
import com.main.toto.member.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Log4j2
class AuctionServiceImplTest {

    @SpyBean
    private AuctionService auctionService;

    @SpyBean
    private BidRepository bidRepository;

    @SpyBean
    private MemberService  memberService;

    @SpyBean
    private BoardService boardService;

    @BeforeAll
    void setUp() {
        dataSetup();
    }

    @DisplayName("남은 시간 테스트")
    @Test
    void getRemainingTime() {
        //given
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        //when
        String remainingTime = auctionService.getRemainingTime(oneHourAgo);
        //then
        assertTrue(remainingTime.compareTo("22:59:59") >= 0 && remainingTime.compareTo("23:00:00") <= 0);
    }

    @DisplayName("입찰 생성 테스트")
    @Test
    void createBid() {
        //given
        BidDTO bidDTO = BidDTO.builder()
                .bno(1L)
                .mid("test2")
                .price(10000L)
                .build();
        //when
        Long bidId = auctionService.createBid(bidDTO);
        //then
        assertNotNull(bidId);
        assertEquals(1L, bidRepository.findById(bidId).get().getBoard().getBno());
    }

    @DisplayName("입찰 생성 실패 테스트")
    @Test
    void createBidFail() {
        //when & then
        assertThrows(IllegalArgumentException.class, () -> auctionService.createBid(new BidDTO(1L,1000L, "test2", 10000L)));
        assertThrows(IllegalArgumentException.class, () -> auctionService.createBid(new BidDTO(2L,1L, "no", 10000L)));
        assertThrows(IllegalArgumentException.class, () -> auctionService.createBid(new BidDTO(3L,1L, "test2", 3000L)));
    }

    @DisplayName("입찰 조회 테스트")
    @Test
    void getBid() {
        //given
        Long bidId = auctionService.createBid(new BidDTO(1L,1L, "test2", 10000L));
        //when
        BidDTO bidDTO = auctionService.getBid(bidId);
        //then
        assertNotNull(bidDTO);
        assertEquals(bidId, bidDTO.getBidId());
        // 입찰 조회 실패 테스트
        assertNull(auctionService.getBid(100L));
    }

    @DisplayName("입찰 삭제 테스트")
    @Test
    void deleteBid() {
        //given
        Long bidId = auctionService.createBid(new BidDTO(1L,1L, "test2", 10000L));
        //when
        auctionService.deleteBid(bidId);
        //then
        assertNull(auctionService.getBid(bidId));
    }

    @DisplayName("입찰 수정 테스트")
    @Test
    void updateBid() {
        //given
        Long bidId = auctionService.createBid(new BidDTO(1L,1L, "test2", 10000L));
        //when
        BidDTO bidDTO = auctionService.updateBid(new BidDTO(bidId, 1L, "test2", 20000L));
        //then
        assertNotNull(bidDTO);
        assertEquals(20000L, bidDTO.getPrice());
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