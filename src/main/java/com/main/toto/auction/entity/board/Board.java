package com.main.toto.auction.entity.board;

import com.main.toto.auction.entity.bid.Bid;
import com.main.toto.bookMark.entity.bookMark.BookMark;
import com.main.toto.global.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"imageSet","bookMarks"})
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    // 후에 유저랑 연관관계 맺어서 관리해야함.
    private Long bookMarkCount = 1L;

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookMark> bookMarks = new HashSet<>();

    // 기본 가격
    private Long price;

    // 지연 로딩, cascade = CascadeType.ALL은 Board가 삭제되면 연관된 이미지도 삭제된다. 그런데 첨부파일만 삭제하는 경우를 위해 orphanRemoval = true를 추가한다.
    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}
    ,orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 50)
    private Set<Bid> bidSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus = AuctionStatus.BIDDING;

    private LocalDateTime auctionStartTime;

    public Duration getRemainTime(){
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(auctionStartTime, now);
        Duration remaining = Duration.ofHours(24).minus(duration);
        return remaining.isNegative() ? Duration.ZERO : remaining;
    }

    public void addImage(String uuid, String fileName){
        BoardImage boardImage = BoardImage.builder()
                .board(this)
                .uuid(uuid)
                .fileName(fileName)
                .ord(imageSet.size())
                .build();
        imageSet.add(boardImage);
    }

    public void clearImages(){

        imageSet.forEach(boardImage -> boardImage.changeBoard(null));

        this.imageSet.clear();
    }

    // 수정해야 하는 경우 Setter 보다는 아래 change와 같은 방식을 활용하자.
    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void addBookMark(BookMark bookMark){
        this.bookMarks.add(bookMark);
    }

    public void removeBookMark(BookMark bookMark){
        this.bookMarks.remove(bookMark);
    }

    public void addBid(Bid bid){
        this.bidSet.add(bid);
    }

    public void changePrice(Long price){
        this.price = price;
    }

    public void changeStatus(AuctionStatus auctionStatus){
        this.auctionStatus = auctionStatus;
    }
}
