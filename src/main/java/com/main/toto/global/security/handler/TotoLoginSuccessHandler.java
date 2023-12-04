package com.main.toto.global.security.handler;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 후에 Referer를 활용하여 로그인 후 이전 페이지로 이동하도록 구현할 것.
 */


@Log4j2
@RequiredArgsConstructor
public class TotoLoginSuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        resultRedirectStrategy(request, response, authentication);
    }

    protected void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.info("------------ Login Success ------------");

        // 객체 가져오기
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        // 이동할 url이 존재하다 = 인증이 필요한 페이지에 방문했었다.
        if(savedRequest != null){
            // 이전 페이지의 url을 가져온다.
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        }else{
            // 직접 로그인 페이지를 방문한 경우는 main으로 이동한다.
            String targetUrl = "/toto/main";
            redirectStrategy.sendRedirect(request, response, targetUrl);
        }
    }
}
