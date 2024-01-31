package com.main.toto.security;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class Oauth2Tests {

    @Autowired
    private MockMvc mvc;


    @DisplayName("네이버 로그인 테스트(접근만 해서 로그인 페이지로 가는지 확인")
    @Test
    void testOauth2Login() throws Exception {
        //given
        //when
        ResultActions resultActions = mvc.perform(get("/oauth2/authorization/naver")
                .with(oauth2Login()
                        .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                        .attributes(attrs -> {
                                attrs.put("username", "username");
                                attrs.put("name", "name");
                                attrs.put("email", "email");
                        })
        ));
        String redirectUrl = resultActions.andReturn().getResponse().getRedirectedUrl();
        int httpStatus = resultActions.andReturn().getResponse().getStatus();
        //then
        assertThat(httpStatus).isEqualTo(302);
        assertThat(redirectUrl).contains("redirect_uri=http://localhost:8080/login/oauth2/code/naver");
    }
}
