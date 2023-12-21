package com.main.toto.repository;

import com.main.toto.domain.board.Board;
import com.main.toto.domain.bookMark.BookMark;
import com.main.toto.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, Long>{
    List<BookMark> findByMember_Mid(String mid);

    boolean existsByMemberAndBoard(Member member, Board board);

    Optional<BookMark> findByMemberAndBoard(Member member, Board board);
}
