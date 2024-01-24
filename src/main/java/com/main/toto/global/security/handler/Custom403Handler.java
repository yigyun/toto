package com.main.toto.global.security.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.PortResolver;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class Custom403Handler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.info("--------ACCESS DENIED--------------");

        response.setStatus(HttpStatus.FORBIDDEN.value());

        //JSON 요청이었는지 확인
        String contentType = request.getHeader("Content-Type");

        boolean jsonRequest = false;
        if (contentType != null) {
            jsonRequest = contentType.startsWith("application/json");
        }
        log.info("isJSON: " + jsonRequest);
        //일반 request
        if (!jsonRequest) {

            // 로그인 성공 후에 원래 가려고 했던 위치로 이동시키기 위한 작업.
            PortResolver portResolver = new PortResolverImpl();
            DefaultSavedRequest savedRequest = new DefaultSavedRequest(request, portResolver);
            request.getSession().setAttribute(("SPRING_SECURITY_SAVED_REQUEST"), savedRequest);

            String requestURI = request.getRequestURI();
            String message = accessDeniedException.getMessage();

            if(message.equals("Access is denied getModify")){
                response.sendRedirect("/toto/main" + "?errors=NO_MODIFY_AUTHORITY");
            }else if ("/toto/board/modify".equals(requestURI)) {
                response.sendRedirect("/toto/member/login?error=NO_MODIFY_PERMISSION");
            } else if ("/toto/board/read".equals(requestURI)) {
                response.sendRedirect("/toto/member/login?error=NO_READ_PERMISSION");
            } else {
                response.sendRedirect("/toto/member/login?error=ACCESS_DENIED");
            }
        }
    }
}