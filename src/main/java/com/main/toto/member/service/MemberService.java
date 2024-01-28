package com.main.toto.member.service;

import com.main.toto.member.dto.member.MemberJoinDTO;

public interface MemberService {
    void join(MemberJoinDTO memberJoinDTO);

    void changePw(String mpassword, String mid);
}
