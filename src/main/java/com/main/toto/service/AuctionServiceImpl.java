package com.main.toto.service;

import com.main.toto.domain.bid.Bid;
import com.main.toto.domain.board.Board;
import com.main.toto.domain.member.Member;
import com.main.toto.dto.BidDTO;
import com.main.toto.repository.BidRepository;
import com.main.toto.repository.BoardRepository;
import com.main.toto.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class AuctionServiceImpl implements AuctionService{

    private final BidRepository bidRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public String getRemainingTime(LocalDateTime regDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(regDate, now);
        Duration remaining = Duration.ofHours(24).minus(duration);
        if (remaining.isNegative()) {
            return "00:00:00";
        } else {
            long hours = remaining.toHours();
            long minutes = remaining.minusHours(hours).toMinutes();
            long seconds = remaining.minusHours(hours).minusMinutes(minutes).getSeconds();
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

    @Override
    public Long createBid(BidDTO bidDTO) {

        Board board = boardRepository.findWithLockByBno(bidDTO.getBno()).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + bidDTO.getBno()));

        Member member = memberRepository.findById(bidDTO.getMid()).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 없습니다. id=" + bidDTO.getMid()));

        if(board.getPrice() < bidDTO.getPrice()){
            board.changePrice(bidDTO.getPrice());
            Bid bid = dtoToEntity(bidDTO);
            bid.addBoard(board);
            bid.addMember(member);
            bidRepository.save(bid);
            return bid.getBidId();
        }else{
            throw new IllegalArgumentException("입찰 금액이 현재 가격보다 낮습니다.");
        }
    }

    @Override
    public BidDTO getBid(Long bidId) {
        return bidRepository.findById(bidId)
                .map(this::entityToDTO)
                .orElse(null);
    }

    @Override
    public void deleteBid(Long bidId) {
        bidRepository.deleteById(bidId);
    }

    @Override
    public BidDTO updateBid(BidDTO bidDTO) {

        Bid bid = bidRepository.findWithLockByMember_MidAndBoard_Bno(bidDTO.getMid(), bidDTO.getBno()).orElseThrow();
        bid.changePrice(bidDTO.getPrice());
        return entityToDTO(bid);
    }
}
