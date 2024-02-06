package com.main.toto.auction.controller;

import com.main.toto.auction.dto.BidDTO;
import com.main.toto.auction.service.auction.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/toto")
public class BidController {

    private final AuctionService auctionService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/board/bid")
    public ResponseEntity<String> placeBid(@RequestBody BidDTO bidDTO){
        log.info("bid Post... : " + bidDTO);
        if(auctionService.getBid(bidDTO.getBno()) != null){
            log.info("bid Post.222.. : " + bidDTO);
            auctionService.updateBid(bidDTO);
            return ResponseEntity.ok("success update");
        }
        log.info("bid Post.333.. : " + bidDTO);
        auctionService.createBid(bidDTO);
        log.info("bid Post.444.. : " + bidDTO);
        return ResponseEntity.ok("success create");
    }
}
