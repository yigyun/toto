package com.main.toto.auction.dto.board;


import com.main.toto.auction.entity.board.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno;

    private String title;

    private String content;

    private String writer;

    private Long price;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modDate;

    // 게시물 등록 시에 이미 첨부파일은 이미 업로드된 파일 정보를 문자열로 받아서 처리한다.
    private List<String> fileNames;

    private BoardCategory boardCategory;

    private Long bookMarkCount;
}
