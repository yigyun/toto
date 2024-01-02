package com.main.toto.domain.board;

import lombok.Getter;

public enum AuctionStatus {

    SUCCESS("낙찰"),
    BIDDING("입찰중"),
    FAIL("유찰");

    @Getter
    private final String description;

    AuctionStatus(String description) {
        this.description = description;
    }

}
