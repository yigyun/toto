package com.main.toto.service;

import com.main.toto.domain.board.Board;
import com.main.toto.domain.board.BoardCategory;
import com.main.toto.dto.board.BoardDTO;
import com.main.toto.repository.BoardRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @Test
    @Transactional
    public void dtoToEntityToDtoTest(){
        Board board = Board.builder()
                .bookMarkCount(1L)
                .boardCategory(BoardCategory.FASHION)
                .bno(1L)
                .content("test1")
                .title("test1")
                .writer("test1")
                .build();

        board.addImage(UUID.randomUUID().toString(), "file" + 1 + ".jpg");

        BoardDTO boardDTO1 = boardService.entityToDTO(board);
        log.info("boardDTO : " + boardDTO1);
        log.info("boardDTO fileNames : " + boardDTO1.getFileNames());
        boardRepository.save(board);
        Board board2 = boardService.dtoToEntity(boardDTO1);
        log.info("board2 : " + board2);
    }
}
