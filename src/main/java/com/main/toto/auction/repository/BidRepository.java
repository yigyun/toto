package com.main.toto.auction.repository;

import com.main.toto.auction.entity.bid.Bid;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long>{
    @EntityGraph(attributePaths = {"board", "member"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Bid> findByMember_MidAndBoard_Bno(String mid, Long bno);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {"board", "member"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("select b from Bid b where b.member.mid = :mid and b.board.bno = :bno")
    Optional<Bid> findWithLockByMember_MidAndBoard_Bno(@Param("mid") String mid,@Param("bno") Long bno);
}
