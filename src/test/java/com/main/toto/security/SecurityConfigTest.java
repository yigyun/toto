package com.main.toto.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SecurityConfigTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("인증없는 접근 테스트")
    @Test
    public void testAuthenticationPage() throws Exception {
        //given
        //when
        ResultActions resultActions = mvc.perform(get("/toto/member/loginNoAuthentication"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatus = resultActions.andReturn().getResponse().getStatus();
        //then
        assertThat(httpStatus).isEqualTo(404);
    }

    @DisplayName("로그인 실패 테스트")
    @Test
    public void testLoginFailure() throws Exception {
        //given
        String username = "test";
        String password = "wrongPwd";
        //when
        ResultActions resultActions = mvc.perform(post("/toto/member/login")
                .param("username", username)
                .param("password", password));
        String redirectUrl = resultActions.andReturn().getResponse().getRedirectedUrl();
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatus = resultActions.andReturn().getResponse().getStatus();

        System.out.println("responseBody: "+responseBody);
        //then
        assertThat(httpStatus).isEqualTo(302);
        assertThat(redirectUrl).contains("errors=INVALID_PASSWORD");
    }

    @DisplayName("로그인 성공 테스트")
    @Test
    public void testLoginSuccess() throws Exception {
        //given
        String username = "test";
        String password = "test";
        //when
        ResultActions resultActions = mvc.perform(post("/toto/member/login")
                .param("username", username)
                .param("password", password));
        String redirectUrl = resultActions.andReturn().getResponse().getRedirectedUrl();
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatus = resultActions.andReturn().getResponse().getStatus();

        //then
        assertThat(httpStatus).isEqualTo(302);
        assertThat(redirectUrl).contains("/toto/main");
    }

    @DisplayName("권한 없는 사용자의 접근 테스트(No 인증)")
    @Test
    public void testAccessDenied() throws Exception {
        //given
        //when
        ResultActions resultActions = mvc.perform(get("/toto/board/read?bno=1"));
        String redirectUrl = resultActions.andReturn().getResponse().getRedirectedUrl();
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatus = resultActions.andReturn().getResponse().getStatus();
        //then
        assertThat(httpStatus).isEqualTo(302);
    }

    @DisplayName("권한 없는 사용자의 접근 테스트(With 인증)")
    @Test
    @WithMockUser(username = "test2", password = "test2", roles = "USER")
    public void testAccessDeniedWithLogin() throws Exception {
        //given
        Long bno = 3L;
        //when
        ResultActions resultActions = mvc.perform(get("/toto/board/modify")
                .param("bno", bno.toString()));
        String redirectUrl = resultActions.andReturn().getResponse().getRedirectedUrl();
        int httpStatus = resultActions.andReturn().getResponse().getStatus();
        //then
        assertThat(httpStatus).isEqualTo(302);
        assertThat(redirectUrl).contains("errors=NO_MODIFY_AUTHORITY");
    }

    @DisplayName("로그인 동시 세션 제한 테스트")
    @Test
    public void testSessionLoginLimit() throws Exception{

        //given
        String username = "test";
        String password = "test";
        //when
        ResultActions resultActions = mvc.perform(post("/toto/member/login")
                .param("username", username)
                .param("password", password));

        int httpStatus1 = resultActions.andReturn().getResponse().getStatus();

        ResultActions resultActions2 = mvc.perform(post("/toto/member/login")
                .param("username", username)
                .param("password", password));
        String redirectUrl = resultActions2.andReturn().getResponse().getRedirectedUrl();
        int httpStatus2 = resultActions.andReturn().getResponse().getStatus();

        //then
        assertThat(httpStatus1).isEqualTo(302);
        assertThat(httpStatus2).isEqualTo(302);
        assertThat(redirectUrl).contains("errors=SESSION_EXCEPTION");
    }
}
