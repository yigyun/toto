package com.main.toto.service;

import com.main.toto.domain.board.Board;
import com.main.toto.domain.board.BoardCategory;
import com.main.toto.domain.bookMark.BookMark;
import com.main.toto.dto.board.BoardDTO;
import com.main.toto.dto.board.BoardListAllDTO;
import com.main.toto.dto.page.PageRequestDTO;
import com.main.toto.dto.page.PageResponseDTO;
import com.main.toto.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final BookMarkService bookMarkService;
    private final ModelMapper modelMapper;

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
        List<BoardDTO> dtoList = new ArrayList<>();

        for(BoardCategory boardCategory : BoardCategory.values()){
            dtoList.add(entityToDTO(boardRepository.findTopByBoardCategoryOrderByBookMarkCountDesc(boardCategory).orElseThrow()));
        }
        return dtoList;
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {

        String keyword = pageRequestDTO.getKeyword();
        BoardCategory boardCategory = pageRequestDTO.getBoardCategory();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(boardCategory, keyword, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithBookMark(PageRequestDTO pageRequestDTO, String mid) {

        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = boardRepository.searchWithBookMark(mid, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

}
