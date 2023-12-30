package com.main.toto.service;

import com.main.toto.domain.board.Board;
import com.main.toto.dto.board.BoardDTO;
import com.main.toto.dto.board.BoardListAllDTO;
import com.main.toto.dto.page.PageRequestDTO;
import com.main.toto.dto.page.PageResponseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    LocalDateTime readDate(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    List<BoardDTO> favoriteMain();

    // 페이지, 이미지, 게시글
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    //페이지, 이미지, 게시글 with BookMark
    PageResponseDTO<BoardListAllDTO> listWithBookMark(PageRequestDTO pageRequestDTO, String mid);

    default Board dtoToEntity(BoardDTO boardDTO){

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .content(boardDTO.getContent())
                .title(boardDTO.getTitle())
                .writer(boardDTO.getWriter())
                .boardCategory(boardDTO.getBoardCategory())
                .bookMarkCount(1L)
                .build();

        if(boardDTO.getFileNames() != null) {
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        } else{
            System.out.println("fileNames is null");
        }
        return board;
    }

    default BoardDTO entityToDTO(Board board){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .content(board.getContent())
                .title(board.getTitle())
                .writer(board.getWriter())
                .boardCategory(board.getBoardCategory())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .bookMarkCount(1L)
                .build();

        List<String> fileNames = board.getImageSet().stream().sorted().map(image ->
                image.getUuid()+"_"+image.getFileName()
        ).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }

}
