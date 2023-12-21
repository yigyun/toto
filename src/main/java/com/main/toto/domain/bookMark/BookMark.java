package com.main.toto.domain.bookMark;

import com.main.toto.domain.board.Board;
import com.main.toto.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bid;

    @ManyToOne
    @JoinColumn(name = "mid")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "bno")
    private Board board;

}
