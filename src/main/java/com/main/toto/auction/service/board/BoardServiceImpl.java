package com.main.toto.auction.service.board;

import com.main.toto.auction.dto.board.BoardDTO;
import com.main.toto.auction.dto.board.BoardListAllDTO;
import com.main.toto.auction.dto.page.PageRequestDTO;
import com.main.toto.auction.dto.page.PageResponseDTO;
import com.main.toto.auction.entity.board.Board;
import com.main.toto.auction.entity.board.BoardCategory;
import com.main.toto.auction.repository.BoardRepository;
import com.main.toto.bookMark.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public Long register(BoardDTO boardDTO) {

        if(boardDTO == null) throw new IllegalArgumentException("게시글 정보가 없습니다.");

        Board board = dtoToEntity(boardDTO);

        return boardRepository.save(board).getBno();
    }

    @Override
    public LocalDateTime readDate(Long bno) {
        return boardRepository.findById(bno).orElseThrow(
                () -> new EntityNotFoundException("해당 게시글이 없습니다.")
        ).getRegDate();
    }

    @Override
    public BoardDTO readOne(Long bno) {
        return entityToDTO(boardRepository.findByIdWithImages(bno).orElseThrow(
                () -> new EntityNotFoundException("해당 게시글이 없습니다.")
        ));
    }

    @Override
    @Transactional
    public void modify(BoardDTO boardDTO) {

        if(boardDTO == null) throw new IllegalArgumentException("게시글 정보가 없습니다.");

        Board board = boardRepository.findById(boardDTO.getBno()).orElseThrow(
                () -> new EntityNotFoundException("해당 게시글이 없습니다.")
        );

        if(boardDTO.getTitle().trim() == null || boardDTO.getContent().trim() == null) throw new IllegalArgumentException("제목 또는 내용이 없습니다.");

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
    @Transactional
    public void remove(Long bno) {
        if(!boardRepository.existsById(bno)) throw new EntityNotFoundException("해당 게시글이 없습니다.");
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

        if(pageRequestDTO == null) throw new IllegalArgumentException("요청이 없습니다.");

        String keyword = pageRequestDTO.getKeyword();
        BoardCategory boardCategory = pageRequestDTO.getBoardCategory();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = null;
        if((pageRequestDTO.getBoardCategory() == null )) result = boardRepository.searchWithKeyword(keyword, pageable);
        else if((pageRequestDTO.getKeyword() == null || pageRequestDTO.getKeyword().trim().length() == 0)) result = boardRepository.searchWithCategory(boardCategory, pageable);
        else result = boardRepository.searchWithAll(boardCategory, keyword, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithBookMark(PageRequestDTO pageRequestDTO, String mid) {

        if(mid == null || pageRequestDTO == null) throw new IllegalArgumentException("회원 정보 또는 요청이 없습니다.");

        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = boardRepository.searchWithBookMark(mid, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public boolean checkWriter(Long bno, String mid) {
        if(bno == null || mid == null) throw new IllegalArgumentException("게시글 번호 또는 아이디 정보가 없습니다.");
        BoardDTO boardDTO = readOne(bno);
        return mid.equals(boardDTO.getWriter());
    }

}
