package com.main.toto.auction.entity.board;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage>{

    @Id
    private String uuid;

    private String fileName;

    private int ord;

    @ManyToOne
    private Board board;

    @Override
    public int compareTo(BoardImage o) {
        return this.ord - o.ord;
    }

    // 연관관계 편의 메소드
    // 나중에 Board가 삭제되면 객체 참조를 변경하기 위함.
    public void changeBoard(Board board){
        this.board = board;
    }
}
