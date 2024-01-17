package com.main.toto.auction.dto.board;

import com.main.toto.auction.entity.board.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListAllDTO {

    private Long bno;

    private String title;

    private String writer;

    private Long price;

    private LocalDateTime regDate;

    private BoardCategory boardCategory;

    private List<BoardImageDTO> boardImages;
}
