package com.main.toto.bookMark.service;

import com.main.toto.auction.entity.board.Board;
import com.main.toto.auction.repository.BoardRepository;
import com.main.toto.bookMark.entity.bookMark.BookMark;
import com.main.toto.bookMark.repository.BookMarkRepository;
import com.main.toto.member.entity.member.Member;
import com.main.toto.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

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

        if(bno == null || bno < 0 || mid == null) throw new IllegalArgumentException("Invalid request.");

        // Member 엔티티를 찾습니다.
        Member member = memberRepository.findById(mid)
                .orElseThrow(() -> new EntityNotFoundException("Member not found: " + mid));

        // Board 엔티티를 찾습니다.
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new EntityNotFoundException("Board not found: " + bno));

        if(bookMarkRepository.existsByMemberAndBoard(member, board)) throw new IllegalArgumentException("Existing BookMark.");

        // 새 BookMark 엔티티를 생성합니다.
        BookMark bookMark = BookMark.builder()
                .board(board)
                .member(member)
                .build();

        // Member와 Board 엔티티에도 BookMark를 추가합니다.
        member.addBookmark(bookMark);
        board.addBookMark(bookMark);

        // 생성한 BookMark 엔티티를 저장합니다.
        bookMarkRepository.save(bookMark);
    }

    @Override
    public void deleteBookMark(Long bno, String mid) {

        if(bno == null || bno < 0 || mid == null) throw new IllegalArgumentException("Invalid request.");

        // Member 엔티티를 찾습니다.
        Member member = memberRepository.findById(mid).orElseThrow(() -> new EntityNotFoundException("Member not found: " + mid));
        // Board 엔티티를 찾습니다.
        Board board = boardRepository.findById(bno).orElseThrow(() -> new EntityNotFoundException("Board not found: " + bno));

        BookMark bookMark = bookMarkRepository.findByMemberAndBoard(member, board).orElseThrow(
                () -> new EntityNotFoundException("BookMark not found: " + mid + ", " + bno)
        );

        board.removeBookMark(bookMark);
        member.removeBookmark(bookMark);
        bookMarkRepository.delete(bookMark);
    }

    @Override
    public List<BookMark> getBookMarkList(String mid) {
        if(mid == null) throw new IllegalArgumentException("Invalid request.");
        return bookMarkRepository.findByMember_Mid(mid);
    }

    @Override
    public boolean existsByMemberAndBoard(String mid, Long bno) {

        if(bno == null || bno < 0 || mid == null) throw new IllegalArgumentException("Invalid request.");


        Member member = memberRepository.findById(mid)
                .orElseThrow(() -> new EntityNotFoundException("Member not found: " + mid));

        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new EntityNotFoundException("Board not found: " + bno));

        return bookMarkRepository.existsByMemberAndBoard(member, board);
    }


}
