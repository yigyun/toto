package com.main.toto.bookMark.service;

import com.main.toto.bookMark.entity.bookMark.BookMark;

import java.util.List;

public interface BookMarkService {
    public void addBookMark(Long bno, String mid);

    public void deleteBookMark(Long bno, String mid);

    public List<BookMark> getBookMarkList(String mid);

    public boolean existsByMemberAndBoard(String mid, Long bno);

}
