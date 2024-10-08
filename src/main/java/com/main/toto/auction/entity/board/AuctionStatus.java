package com.main.toto.auction.entity.board;

import lombok.Getter;

public enum AuctionStatus {

    SUCCESS("낙찰"),
    BIDDING("입찰중"),
    FAIL("유찰"),

    END("종료");

    @Getter
    private final String description;

    AuctionStatus(String description) {
        this.description = description;
    }

}
