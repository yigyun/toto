package com.main.toto.repository;

import com.main.toto.domain.bid.Bid;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long>{
    @EntityGraph(attributePaths = {"board", "member"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Bid> findByMember_MidAndBoard_Bno(String mid, Long bno);

}
