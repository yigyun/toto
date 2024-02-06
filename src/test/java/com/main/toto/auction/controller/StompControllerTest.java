package com.main.toto.auction.controller;

import com.main.toto.auction.dto.board.BoardDTO;
import com.main.toto.auction.service.auction.AuctionTimeService;
import com.main.toto.auction.service.board.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import static org.mockito.ArgumentMatchers.any;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StompControllerTest {

    @Mock
    private BoardService boardService;

    @Mock
    private AuctionTimeService auctionTimeService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private StompController stompController;

    @DisplayName("경매 시간을 가져온다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getAuctionTime() throws Exception {
        // Arrange
        LocalDateTime expectedDate = LocalDateTime.now();
        when(boardService.readDate(anyLong())).thenReturn(expectedDate);
        when(auctionTimeService.getRemainingTime(any(LocalDateTime.class))).thenReturn("10:00:00");

        Map<String, String> payload = new HashMap<>();
        payload.put("bno", "1");

        // Act
        stompController.getAuctionTime(payload);

        // Assert
        verify(boardService, times(1)).readDate(anyLong());
        verify(auctionTimeService, times(1)).getRemainingTime(any(LocalDateTime.class));
        verify(messagingTemplate, times(1)).convertAndSend(any(String.class), any(Object.class));
    }

    @Test
    void handleEntityNotFoundException() {
    }

    @Test
    void handleIllegalArgumentException() {
    }
}