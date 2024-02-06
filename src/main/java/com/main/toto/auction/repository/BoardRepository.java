package com.main.toto.auction.repository;

import com.main.toto.auction.entity.board.Board;
import com.main.toto.auction.entity.board.BoardCategory;
import com.main.toto.auction.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findWithLockByBno(@Param("bno") Long bno);

    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findByIdWithImages(@Param("bno") Long bno);

    @EntityGraph(attributePaths = {"imageSet"})
    @Query("SELECT b FROM Board b WHERE b.boardCategory = :boardCategory AND b.bookMarkCount = (SELECT MAX(b2.bookMarkCount) FROM Board b2 WHERE b2.boardCategory = :boardCategory) " +
            "AND b.bno = (SELECT MAX(b3.bno) FROM Board b3 WHERE b3.boardCategory = :boardCategory AND b3.bookMarkCount = b.bookMarkCount)")
    Optional<Board> findTopByBoardCategoryOrderByBookMarkCountDesc(@Param("boardCategory") BoardCategory boardCategory);

    @EntityGraph(attributePaths = {"imageSet"})
    @Query("SELECT b FROM Board b WHERE b.boardCategory = :boardCategory")
    List<Board> findByBoardCategory(@Param("boardCategory") BoardCategory boardCategory);


    List<Board> findAllByRegDateBefore(LocalDateTime threshold);
}
