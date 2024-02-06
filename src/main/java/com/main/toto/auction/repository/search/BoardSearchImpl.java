package com.main.toto.auction.repository.search;

import com.main.toto.auction.dto.board.BoardImageDTO;
import com.main.toto.auction.dto.board.BoardListAllDTO;
import com.main.toto.auction.entity.board.Board;
import com.main.toto.auction.entity.board.BoardCategory;
import com.main.toto.auction.entity.board.QBoard;
import com.main.toto.bookMark.entity.bookMark.BookMark;
import com.main.toto.bookMark.repository.BookMarkRepository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static com.main.toto.auction.entity.board.QBoard.*;


/**
 * QuerydslRepositorySupport를 상속받아서 구현하는 방식으로 진행.
 * JPQLQueryFactory 객체를 내부적으로 관리해줌.
 */

@Log4j2
@Repository
public class BoardSearchImpl extends Querydsl5RepositorySupport implements BoardSearch{

        private final BookMarkRepository bookMarkRepository;

        public BoardSearchImpl(BookMarkRepository bookMarkRepository){
            super(Board.class);
            this.bookMarkRepository = bookMarkRepository;
        }

        @Override
        public Page<Board> seachPage(Pageable pageable) {

            QBoard board = QBoard.board;
            JPAQuery<Board> jpaQuery = selectFrom(board);

            BooleanBuilder booleanBuilder = new BooleanBuilder();
            booleanBuilder.or(board.title.contains("t"));
            booleanBuilder.or(board.content.contains("t"));

            jpaQuery.where(booleanBuilder);
            jpaQuery.where(board.bno.gt(0L));

            this.getQuerydsl().applyPagination(pageable, jpaQuery);

            List<Board> list = jpaQuery.fetch();

            // fetchCount의 경우 향후 미지원임.
            /**
             * long count = queryFactory.select(board.count()).from(board).fetchOne();
             * 이런 식으로 직접 카운트를 가져와야함.
             */
            long count = jpaQuery.fetchCount();

            return null;
        }

        @Override
        public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

            QBoard board = QBoard.board;
            JPAQuery<Board> jpaQuery = selectFrom(board);

            if((types != null && types.length > 0) && keyword != null) {
                BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

                for (String type : types) {

                    switch (type) {
                        case "t":
                            booleanBuilder.or(board.title.contains(keyword));
                            break;
                        case "c":
                            booleanBuilder.or(board.content.contains(keyword));
                            break;
                        case "w":
                            booleanBuilder.or(board.writer.contains(keyword));
                            break;
                    }
                }
                jpaQuery.where(booleanBuilder);
            }

            jpaQuery.where(board.bno.gt(0L));
            return null;
        }

        @Override
        public Page<BoardListAllDTO> searchWithAll(BoardCategory boardCategory, String keyword, Pageable pageable) {

            QBoard board = QBoard.board;

            JPAQuery<Board> boardJPAQuery = selectFrom(board);
            if((boardCategory != null) && keyword != null && !keyword.trim().isEmpty()){
                BooleanBuilder booleanBuilder = new BooleanBuilder();
                booleanBuilder.and(board.boardCategory.eq(boardCategory));
                booleanBuilder.and(board.title.contains(keyword).or(board.content.contains(keyword)));
                boardJPAQuery.where(booleanBuilder);
            }

            boardJPAQuery.groupBy(board);

            getQuerydsl().applyPagination(pageable, boardJPAQuery);

            List<Board> boardList = boardJPAQuery.fetch();

            List<BoardListAllDTO> boardDTOList = boardList.stream().map(board1 -> {

                BoardListAllDTO boardDTO = BoardListAllDTO.builder()
                        .bno(board1.getBno())
                        .title(board1.getTitle())
                        .writer(board1.getWriter())
                        .regDate(board1.getRegDate())
                        .price(board1.getPrice())
                        .boardCategory(board1.getBoardCategory())
                        // 후에 price 추가.
                        // 시간도 추가
                        .build();

                List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted().map(boardImage -> {
                    return BoardImageDTO.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build();
                }).collect(Collectors.toList());

                boardDTO.setBoardImages(imageDTOS);

                log.info("BoardSearchImpl check: " + boardDTO.getBoardImages());

                return boardDTO;
            }).collect(Collectors.toList());

            long totalCount = boardJPAQuery.fetchCount();

            return new PageImpl<>(boardDTOList, pageable, totalCount);
        }

    @Override
    public Page<BoardListAllDTO> searchWithKeyword(String keyword, Pageable pageable) {

        QBoard board = QBoard.board;

        JPAQuery<Board> boardJPAQuery = selectFrom(board);
        if(keyword != null && !keyword.trim().isEmpty()){
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            booleanBuilder.and(board.title.contains(keyword).or(board.content.contains(keyword)));
            boardJPAQuery.where(booleanBuilder);
        }


        getQuerydsl().applyPagination(pageable, boardJPAQuery);

        List<Board> boardList = boardJPAQuery.fetch();

        List<BoardListAllDTO> boardDTOList = boardList.stream().map(board1 -> {

            BoardListAllDTO boardDTO = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .price(board1.getPrice())
                    .boardCategory(board1.getBoardCategory())
                    .build();

            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted().map(boardImage -> {
                return BoardImageDTO.builder()
                        .uuid(boardImage.getUuid())
                        .fileName(boardImage.getFileName())
                        .ord(boardImage.getOrd())
                        .build();
            }).collect(Collectors.toList());

            boardDTO.setBoardImages(imageDTOS);

            log.info("BoardSearchImpl check: " + boardDTO.getBoardImages());

            return boardDTO;
        }).collect(Collectors.toList());

        long totalCount = boardJPAQuery.fetchCount();

        return new PageImpl<>(boardDTOList, pageable, totalCount);
    }

    @Override
    public Page<BoardListAllDTO> searchWithCategory(BoardCategory boardCategory, Pageable pageable) {
        if(boardCategory == null) {
            return null;
        }

        QBoard board = QBoard.board;

        JPAQuery<Board> boardJPAQuery = selectFrom(board);
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            booleanBuilder.and(board.boardCategory.eq(boardCategory));
            boardJPAQuery.where(booleanBuilder);


        getQuerydsl().applyPagination(pageable, boardJPAQuery);

        List<Board> boardList = boardJPAQuery.fetch();

        List<BoardListAllDTO> boardDTOList = boardList.stream().map(board1 -> {

            BoardListAllDTO boardDTO = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .price(board1.getPrice())
                    .boardCategory(board1.getBoardCategory())
                    .build();

            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted().map(boardImage -> {
                return BoardImageDTO.builder()
                        .uuid(boardImage.getUuid())
                        .fileName(boardImage.getFileName())
                        .ord(boardImage.getOrd())
                        .build();
            }).collect(Collectors.toList());

            boardDTO.setBoardImages(imageDTOS);

            return boardDTO;
        }).collect(Collectors.toList());

        long totalCount = boardJPAQuery.fetchCount();

        return new PageImpl<>(boardDTOList, pageable, totalCount);
    }

    @Override
        public Page<BoardListAllDTO> searchWithBookMark(String mid, Pageable pageable) {
            List<BookMark> bookMarks = bookMarkRepository.findByMember_Mid(mid);

            JPAQuery<BoardListAllDTO> query = selectFrom(board)
                    .where(board.in(
                            bookMarks.stream()
                                    .map(BookMark::getBoard)
                                    .collect(Collectors.toList())
                    ))
                    .select(Projections.bean(BoardListAllDTO.class,
                            board.bno,
                            board.title,
                            board.writer,
                            board.regDate,
                            board.boardCategory
                    ));

            getQuerydsl().applyPagination(pageable, query);

            List<BoardListAllDTO> boardList = query.fetch();
            long totalCount = query.fetchCount();

            return new PageImpl<>(boardList, pageable, totalCount);
        }
    }
