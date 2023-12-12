package com.main.toto.repository;

import com.main.toto.domain.board.Board;
import com.main.toto.domain.board.BoardCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findByIdWithImages(Long bno);

    @EntityGraph(attributePaths = {"imageSet"})
    @Query("SELECT b FROM Board b WHERE b.boardCategory = :boardCategory AND b.bookMarkCount = (SELECT MAX(b2.bookMarkCount) FROM Board b2 WHERE b2.boardCategory = :boardCategory)")
    Optional<Board> findTopByBoardCategoryOrderByBookMarkCountDesc(@Param("boardCategory") BoardCategory boardCategory);

    @EntityGraph(attributePaths = {"imageSet"})
    @Query("SELECT b FROM Board b WHERE b.boardCategory = :boardCategory")
    Optional<Board> findByBoardCategory(@Param("boardCategory") BoardCategory boardCategory);
}
