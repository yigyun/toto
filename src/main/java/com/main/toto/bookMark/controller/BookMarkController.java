package com.main.toto.bookMark.controller;

import com.main.toto.bookMark.dto.bookMark.BookMarkDTO;
import com.main.toto.bookMark.service.BookMarkService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
