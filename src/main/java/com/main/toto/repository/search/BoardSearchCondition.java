package com.main.toto.repository.search;

import com.main.toto.domain.board.BoardCategory;
import lombok.Data;

@Data
public class BoardSearchCondition {

    private String keyword;

    BoardCategory boardCategory;
}
