package com.main.toto.global.security.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 보호된 리소스에 접근하려는 시도에 대한 핸들러로 401을 반환하는데 사용.
@Log4j2
public class Custom401Handler implements AuthenticationFailureHandler, AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.info("--------AuthenticationEntryPoint--------------");
        response.sendRedirect("/toto/member/login?errors=ACCESS_DENIED");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof UsernameNotFoundException) {
            response.sendRedirect("/toto/member/login?errors=INVALID_USERNAME");
        } else if (exception instanceof BadCredentialsException) {
            response.sendRedirect("/toto/member/login?errors=INVALID_PASSWORD");
        } else if(exception instanceof SessionAuthenticationException) {
            response.sendRedirect("/toto/member/login?errors=SESSION_EXCEPTION");
        } else {
            response.sendRedirect("/toto/member/login?errors=AUTHENTICATION_FAILED");
        }
    }
}
