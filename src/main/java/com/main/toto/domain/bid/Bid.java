package com.main.toto.domain.bid;

import com.main.toto.domain.board.Board;
import com.main.toto.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    @ManyToOne
    @JoinColumn(name = "mid")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "bno")
    private Board board;

    private Long bidAmount;

    @Enumerated(EnumType.STRING)
    private BidStatus bidStatus;
}
