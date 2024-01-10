package com.main.toto.auction.service.board;

import com.main.toto.auction.dto.board.BoardDTO;
import com.main.toto.auction.dto.board.BoardListAllDTO;
import com.main.toto.auction.dto.page.PageRequestDTO;
import com.main.toto.auction.dto.page.PageResponseDTO;
import com.main.toto.auction.entity.board.Board;

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
                .price(boardDTO.getPrice())
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
                .price(board.getPrice())
                .build();

        List<String> fileNames = board.getImageSet().stream().sorted().map(image ->
                image.getUuid()+"_"+image.getFileName()
        ).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }

}
