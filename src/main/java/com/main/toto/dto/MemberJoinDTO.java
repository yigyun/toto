package com.main.toto.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MemberJoinDTO {

    private String mid;
    private String mpassword;
    private String email;
    private boolean del;
    private boolean social;
}
