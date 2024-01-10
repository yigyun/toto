package com.main.toto.member.dto.member;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class MemberJoinDTO {


    @NotBlank(message = "아이디를 입력하세요.")
    @Size(min = 4, message = "4자리 이상 입력하세요.")
    private String mid;

    @NotEmpty(message = "비밀번호를 입력하세요.")
    @Size(min = 4, message = "4자리 이상 입력하세요.")
    private String mpassword;

    @NotEmpty(message = "이메일을 입력하세요.")
    @Email(message = "이메일 형식에 맞게 입력하세요.")
    private String email;
    private boolean del;
    private boolean social;
}
