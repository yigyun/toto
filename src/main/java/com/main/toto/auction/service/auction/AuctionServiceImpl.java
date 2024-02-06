package com.main.toto.auction.service.auction;

import com.main.toto.auction.dto.BidDTO;
import com.main.toto.auction.entity.bid.Bid;
import com.main.toto.auction.entity.board.Board;
import com.main.toto.auction.repository.BidRepository;
import com.main.toto.auction.repository.BoardRepository;
import com.main.toto.member.entity.member.Member;
import com.main.toto.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

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
            try {
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
            } catch (OptimisticLockingFailureException e) {
                throw new IllegalArgumentException("다른 사용자가 이미 입찰하였습니다. 다시 시도해주세요.");
            }
    }

    @Override
    public BidDTO getBid(Long bidId) {
        Optional<Bid> optionalBid = bidRepository.findById(bidId);
        if(optionalBid.isPresent()) {
            return entityToDTO(optionalBid.get());
        }
        return null;
    }

    @Override
    public void deleteBid(Long bidId) {
        bidRepository.deleteById(bidId);
    }

    @Override
    public BidDTO updateBid(BidDTO bidDTO) {
        Optional<Bid> bid = bidRepository.findWithLockByMember_MidAndBoard_Bno(bidDTO.getMid(), bidDTO.getBno());
        if(bid.isPresent()){
            Bid entity = bid.get();
            entity.changePrice(bidDTO.getPrice());
            return  entityToDTO(entity);
        }else throw new IllegalArgumentException("해당 입찰이 없습니다.");
    }
}
