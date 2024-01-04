package com.main.toto.service;

import com.main.toto.domain.bid.Bid;
import com.main.toto.dto.BidDTO;

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
