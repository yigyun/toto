package com.main.toto.service;

import com.main.toto.dto.MemberJoinDTO;

public interface MemberService {
    static class MidExistException extends Exception{}

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;

}
