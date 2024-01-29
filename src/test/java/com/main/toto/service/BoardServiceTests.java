package com.main.toto.service;

import com.main.toto.auction.service.board.BoardService;
import com.main.toto.auction.entity.board.Board;
import com.main.toto.auction.entity.board.BoardCategory;
import com.main.toto.auction.dto.board.BoardDTO;
import com.main.toto.auction.repository.BoardRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
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

    /**
     * 게시글 등록 테스트
     */

    @Test
    @DisplayName("게시글 등록 테스트")
    public void testRegister(){

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .bookMarkCount(1L)
                .boardCategory(BoardCategory.FASHION)
                .build();

        log.info("boardDTO: "+boardDTO);

        Long bno = boardService.register(boardDTO);

        log.info("bno: "+bno);
    }



    @Test
    public void testMain(){

        log.info("board main test");

        Board board = Board.builder()
                .bookMarkCount(1L)
                .boardCategory(BoardCategory.FASHION)
                .bno(1L)
                .content("test1")
                .title("test1")
                .writer("test1")
                .build();

        board.addImage(UUID.randomUUID().toString(), "file" + 1 + ".jpg");


    }


    /**
     * dto -> entity -> dto 테스트
     */
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
