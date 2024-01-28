package com.main.toto.bookMark.controller;

import com.main.toto.bookMark.dto.bookMark.BookMarkDTO;
import com.main.toto.bookMark.service.BookMarkService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
@Log4j2
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @ApiOperation(value = "Add BookMark", notes = "Post 방식으로 북마크 추가하기")
    @PostMapping("/bookMark/add")
    public ResponseEntity<Boolean> addBookMark(@RequestBody BookMarkDTO bookMarkDTO) {


        log.info("BookMark ADD mid, bno : " + bookMarkDTO.getMid() + ", " + bookMarkDTO.getBno());

        if (bookMarkDTO.getMid() != null && (bookMarkDTO.getBno() != null) && (bookMarkDTO.getBno() >= 0)) {
            bookMarkService.addBookMark(bookMarkDTO.getBno(), bookMarkDTO.getMid());
            return new ResponseEntity<>(true, HttpStatus.OK);
        }

        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Delete BookMark", notes = "Delete 방식으로 북마크 삭제하기")
    @DeleteMapping("/bookMark/delete")
    public ResponseEntity<Boolean> deleteBookMark(@RequestBody BookMarkDTO bookMarkDTO) {

        if (bookMarkDTO.getMid() != null && (bookMarkDTO.getBno() != null) && (bookMarkDTO.getBno() >= 0)) {
            bookMarkService.deleteBookMark(bookMarkDTO.getBno(), bookMarkDTO.getMid());
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

        return new ResponseEntity<>(true, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("대상을 찾을 수 없습니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // Log the exception
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("잘못된 요청입니다.");
    }
}
