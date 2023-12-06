package com.main.toto.service;

import com.main.toto.dto.member.MemberJoinDTO;

public interface MemberService {
    static class MidExistException extends Exception{}

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;

    void changePw(String mpassword, String mid);

}
