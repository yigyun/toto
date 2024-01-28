package com.main.toto.auction.controller;

import com.main.toto.auction.service.auction.AuctionTimeService;
import com.main.toto.auction.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
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

    @MessageMapping("/auctionTime")
    public void getAuctionTime(Map<String, String> payload) {
        log.info("getAuctionTime() called");

        String bnoStr = payload.get("bno");
        if(bnoStr == null) throw new IllegalArgumentException("bno is null");

        Long bno = Long.parseLong(bnoStr);

        LocalDateTime date = boardService.readDate(bno);
        if (date == null) throw new EntityNotFoundException("Board with bno " + bno + " not found");
        String remainingTime = auctionTimeService.getRemainingTime(date);

        Map<String, Object> response = new HashMap<>();
        response.put("remainingTime", remainingTime);

        messagingTemplate.convertAndSend("/topic/auctionTime/" + bno, response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("대상을 찾을 수 없습니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // Log the exception
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("해당 게시물이 존재하지 않습니다.");
    }
}
