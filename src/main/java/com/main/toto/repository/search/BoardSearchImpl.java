package com.main.toto.repository.search;

import com.main.toto.domain.board.Board;
import com.main.toto.domain.board.QBoard;
import com.main.toto.dto.board.BoardListAllDTO;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{

        public BoardSearchImpl(){
            super(Board.class);
        }

        @Override
        public Page<Board> seachPage(Pageable pageable) {

            QBoard board = QBoard.board;

            JPQLQuery<Board> jpqlQuery = from(board);


            return null;
        }

        @Override
        public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
            return null;
        }

        @Override
        public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
            return null;
        }

}
