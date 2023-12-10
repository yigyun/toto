package com.main.toto.service;

import com.main.toto.domain.board.Board;
import com.main.toto.domain.board.BoardCategory;
import com.main.toto.dto.board.BoardDTO;
import com.main.toto.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    @Override
    public Long register(BoardDTO boardDTO) {

        Board board = dtoToEntity(boardDTO);

        return boardRepository.save(board).getBno();
    }

    @Override
    public BoardDTO readOne(Long bno) {
        return entityToDTO(boardRepository.findByIdWithImages(bno).orElseThrow());
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Board board = boardRepository.findById(boardDTO.getBno()).orElseThrow();
        // 제목, 내용 수정
        board.change(boardDTO.getTitle(), boardDTO.getContent());
        // 이미지 수정
        board.clearImages();
        if(boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }
        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public List<BoardDTO> favoriteMain() {
        List<BoardDTO> dtoList = null;
        log.info("board test in");
        Board board1 = boardRepository.findByBoardCategoryAndBookMarkCount(BoardCategory.FASHION).orElseThrow();
        dtoList.add(entityToDTO(board1));
        log.info("board test1");
        dtoList.add(entityToDTO(boardRepository.findByBoardCategoryAndBookMarkCount(BoardCategory.MOVIE).orElseThrow()));
        log.info("board test2");
        dtoList.add(entityToDTO(boardRepository.findByBoardCategoryAndBookMarkCount(BoardCategory.MUSIC).orElseThrow()));
        log.info("board test3");
        return dtoList;
    }
}
