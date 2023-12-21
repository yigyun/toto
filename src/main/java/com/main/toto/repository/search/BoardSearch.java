package com.main.toto.repository.search;

import com.main.toto.domain.board.Board;
import com.main.toto.domain.board.BoardCategory;
import com.main.toto.dto.board.BoardListAllDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {

    Page<Board> seachPage(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListAllDTO> searchWithAll(BoardCategory boardCategory, String keyword, Pageable pageable);

    Page<BoardListAllDTO> searchWithBookMark(String mid, Pageable pageable);
}
