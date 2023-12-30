package com.main.toto.domain.bid;

import lombok.Getter;

public enum BidStatus {

    SUCCESS("낙찰"),
    BIDDING("입찰중"),
    FAIL("유찰");

    @Getter
    private final String description;

    BidStatus(String description) {
        this.description = description;
    }

}
