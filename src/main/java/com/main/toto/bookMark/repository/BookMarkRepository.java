package com.main.toto.bookMark.repository;

import com.main.toto.auction.entity.board.Board;
import com.main.toto.bookMark.entity.bookMark.BookMark;
import com.main.toto.member.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, Long>{
    List<BookMark> findByMember_Mid(String mid);

    boolean existsByMemberAndBoard(Member member, Board board);

    Optional<BookMark> findByMemberAndBoard(Member member, Board board);
}
