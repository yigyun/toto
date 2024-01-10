package com.main.toto.member.dto.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class MemberPwModifyDTO {
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(min = 4, message = "비밀번호는 4자리 이상입니다.")
    private String mpassword;
}
