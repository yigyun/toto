package com.main.toto.service;

import com.main.toto.domain.bookMark.BookMark;

import java.util.List;

public interface BookMarkService {
    public void addBookMark(Long bno, String mid);

    public void deleteBookMark(Long bno, String mid);

    public List<BookMark> getBookMarkList(String mid);

    public boolean existsByMemberAndBoard(String mid, Long bno);

}
