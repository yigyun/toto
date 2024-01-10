package com.main.toto.auction.service.auction;

import com.main.toto.auction.dto.BidDTO;
import com.main.toto.auction.entity.bid.Bid;

import java.time.LocalDateTime;

public interface AuctionService {

    public String getRemainingTime(LocalDateTime regDate);

    Long createBid(BidDTO bidDTO);

    BidDTO getBid(Long bidId);

    void deleteBid(Long bidId);

    BidDTO updateBid(BidDTO bidDTO);

    default Bid dtoToEntity(BidDTO bidDTO){
        Bid bid = Bid.builder()
                .bidId(bidDTO.getBidId())
                .price(bidDTO.getPrice())
                .build();
        return bid;
    }

    default BidDTO entityToDTO(Bid bid){
        BidDTO bidDTO = BidDTO.builder()
                .bidId(bid.getBidId())
                .price(bid.getPrice())
                .build();
        return bidDTO;
    }

}
