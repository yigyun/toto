package com.main.toto.auction.controller;

import com.main.toto.auction.service.auction.AuctionTimeService;
import com.main.toto.auction.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class StompController {

    private final BoardService boardService;
    private final AuctionTimeService auctionTimeService;
    private final SimpMessagingTemplate messagingTemplate;

//    @MessageMapping("/auctionTime")
//    @SendTo("/topic/auctionTime/{bno}")
//    public ResponseEntity<Map<String, Object>> getAuctionTime(Map<String, String> payload) {
//        log.info("getAuctionTime() called");
//        String bnoStr = payload.get("bno");
//        Long bno = Long.parseLong(bnoStr);
//
//        LocalDateTime date = boardService.readDate(bno);
//        String remainingTime = auctionTimeService.getRemainingTime(date);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("remainingTime", remainingTime);
//
//        return ResponseEntity.ok(response);
//    }

    @MessageMapping("/auctionTime")
    public void getAuctionTime(Map<String, String> payload) {
        log.info("getAuctionTime() called");
        String bnoStr = payload.get("bno");
        Long bno = Long.parseLong(bnoStr);

        LocalDateTime date = boardService.readDate(bno);
        String remainingTime = auctionTimeService.getRemainingTime(date);

        Map<String, Object> response = new HashMap<>();
        response.put("remainingTime", remainingTime);

        messagingTemplate.convertAndSend("/topic/auctionTime/" + bno, response);
    }
}
