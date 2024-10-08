package com.main.toto.auction.entity.bid;

import com.main.toto.auction.entity.board.Board;
import com.main.toto.member.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bno")
    private Board board;

    private Long price;

    public void changePrice(Long price){
        this.price = price;
    }

    public void addMember(Member member){
        this.member = member;
    }

    public void addBoard(Board board){
        this.board = board;
    }
}
