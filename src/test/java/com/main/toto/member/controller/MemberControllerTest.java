package com.main.toto.member.controller;

import com.main.toto.config.ControllerTestConfig;
import com.main.toto.member.dto.member.MemberJoinDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
class MemberControllerTest extends ControllerTestConfig {

    @Autowired
    private EntityManager testEntityManager;

    @AfterEach
    void setUp() {
//        testEntityManager.clear();
        SecurityContextHolder.clearContext();
    }

    @DisplayName("로그인 페이지 요청시 로그인 페이지를 반환한다.")
    @Test
    void loginGET_WhenPrincipalIsNull_ReturnsLoginView() throws Exception{

        SecurityContextHolder.clearContext();

        mvc.perform(get("/toto/member/login")
                        .param(("error"), "error")
                        .param("logout", "logout")
                        .with(request -> {
                            request.setUserPrincipal(null);
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(view().name("toto/member/login"));
    }

    @DisplayName("로그인 페이지 요청시 로그인 페이지를 반환한다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void loginGET_WhenPrincipalIsNotNull_ReturnsMainView() throws Exception{
        mvc.perform(get("/toto/member/login")
                        .param(("error"), "error")
                        .param("logout", "logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/toto/main"));
    }

    @DisplayName("회원가입 페이지 요청시 회원가입 페이지를 반환한다.")
    @Test
    void joinGET() throws Exception {
        mvc.perform(get("/toto/member/join"))
                .andExpect(status().isOk())
                .andExpect(view().name("toto/member/join"));
    }

    @DisplayName("회원가입 후 로그인 페이지로 리다이렉트한다.")
    @Test
    void joinPOST() throws Exception {
        mvc.perform(post("/toto/member/join")
                        .param("mid", "test1")
                        .param("mpassword", "1111")
                        .param("email", "email1@aaa.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/toto/member/login"))
                .andExpect(flash().attribute("result", "success"));
    }

    @DisplayName("회원가입 바인딩 실패(bean validation) 후 다시 회원가입 페이지.")
    @Test
    void joinPOSTBindingFail() throws Exception {
        mvc.perform(post("/toto/member/join")
                        .param("mid", "test2")
                        .param("mpassword", "1")
                        .param("email", "email1@aaa.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("toto/member/join"));
    }

    @DisplayName("비밀번호 수정 페이지 요청시 비밀번호 수정 페이지를 반환한다.")
    @Test
    void pwModifyGET() throws Exception{
        mvc.perform(get("/toto/member/modify"))
                .andExpect(status().isOk())
                .andExpect(view().name("toto/member/modify"));
    }

    @DisplayName("비밀번호 수정 후 로그인 페이지로 리다이렉트한다.")
    @WithMockUser(username = "test3", roles = "USER")
    @Test
    void pwModifyPOST() throws Exception {

        //given
        MemberJoinDTO memberJoinDTO = new MemberJoinDTO();
        memberJoinDTO.setMid("test3");
        memberJoinDTO.setMpassword("1111");
        memberJoinDTO.setEmail("test1@test.com");
        memberService.join(memberJoinDTO);
        //when then
        mvc.perform(post("/toto/member/modify")
                .param("mpassword", "3333"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/toto/main"));
    }

    @DisplayName("비밀번호 수정(바인딩 실패) 후 다시 수정페이지로 돌아온다.")
    @WithMockUser(username = "test4", roles = "USER")
    @Test
    void pwModifyPOSTFail() throws Exception {

        //given
        MemberJoinDTO memberJoinDTO = new MemberJoinDTO();
        memberJoinDTO.setMid("test4");
        memberJoinDTO.setMpassword("1111");
        memberJoinDTO.setEmail("test1@test.com");
        memberService.join(memberJoinDTO);
        //when then
        mvc.perform(post("/toto/member/modify")
                        .param("mpassword", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("toto/member/modify"));
    }
}