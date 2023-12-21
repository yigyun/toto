package com.main.toto.service;

import com.main.toto.domain.board.Board;
import com.main.toto.domain.bookMark.BookMark;
import com.main.toto.domain.member.Member;
import com.main.toto.repository.BoardRepository;
import com.main.toto.repository.BookMarkRepository;
import com.main.toto.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BookMarkServiceImpl  implements BookMarkService{

    private final BookMarkRepository bookMarkRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    public void addBookMark(Long bno, String mid) {
        // Member 엔티티를 찾습니다.
        Member member = memberRepository.findById(mid)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + mid));

        // Board 엔티티를 찾습니다.
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("Board not found: " + bno));

        // 새 BookMark 엔티티를 생성합니다.
        BookMark bookMark = BookMark.builder()
                .board(board)
                .member(member)
                .build();

        // 생성한 BookMark 엔티티를 저장합니다.
        bookMarkRepository.save(bookMark);

        // Member와 Board 엔티티에도 BookMark를 추가합니다.
        member.addBookmark(bookMark);
        board.addBookMark(bookMark);
    }

    @Override
    public void deleteBookMark(Long bno, String mid) {
        // Member 엔티티를 찾습니다.
        Member member = memberRepository.findById(mid).orElseThrow();
        // Board 엔티티를 찾습니다.
        Board board = boardRepository.findById(bno).orElseThrow();

        BookMark bookMark = bookMarkRepository.findByMemberAndBoard(member, board).orElseThrow();

        board.removeBookMark(bookMark);
        member.removeBookmark(bookMark);
        bookMarkRepository.delete(bookMark);
    }

    @Override
    public List<BookMark> getBookMarkList(String mid) {
        return bookMarkRepository.findByMember_Mid(mid);
    }

    @Override
    public boolean existsByMemberAndBoard(String mid, Long bno) {

        Member member = memberRepository.findById(mid)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + mid));

        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("Board not found: " + bno));

        return bookMarkRepository.existsByMemberAndBoard(member, board);
    }
}
