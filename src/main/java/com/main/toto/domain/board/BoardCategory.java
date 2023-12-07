package com.main.toto.domain.board;

import lombok.Getter;

/**
 * 패션, 음식, 여행, 뷰티, 건강, 스포츠, 취미, 게임, 영화, 음악, 책, 동물, 기타
 */

public enum BoardCategory {
    FASHION("패션"), FOOD("음식"),
    TRAVEL("여행"), BEAUTY("뷰티"),
    HEALTH("건강"), SPORTS("스포츠"),
    HOBBY("취미"), GAME("게임"),
    MOVIE("영화"), MUSIC("음악"),
    BOOK("책"), ANIMAL("동물"), ETC("기타");

    @Getter
    private final String description;

    BoardCategory(String description) {
        this.description = description;
    }

}
