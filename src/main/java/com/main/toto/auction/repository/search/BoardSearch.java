package com.main.toto.auction.repository.search;

import com.main.toto.auction.dto.board.BoardListAllDTO;
import com.main.toto.auction.entity.board.Board;
import com.main.toto.auction.entity.board.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {

    Page<Board> seachPage(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListAllDTO> searchWithAll(BoardCategory boardCategory, String keyword, Pageable pageable);

    Page<BoardListAllDTO> searchWithBookMark(String mid, Pageable pageable);
}
