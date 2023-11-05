package com.main.toto.dto;

import lombok.Data;

@Data
public class MemberJoinDTO {
    private String mid;
    private String mpassword;
    private String email;
    private boolean del;
    private boolean social;
}
