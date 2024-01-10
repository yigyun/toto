package com.main.toto.auction.service.auction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuctionTimeService {
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

}
