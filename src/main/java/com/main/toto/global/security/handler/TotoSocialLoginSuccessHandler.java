package com.main.toto.global.security.handler;

import com.main.toto.global.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class TotoSocialLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("------------ Oauth2 Login Success ------------");
        DefaultSavedRequest savedRequest = (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();

        String encodePw = memberSecurityDTO.getMpassword();

        if(memberSecurityDTO.isSocial() && (memberSecurityDTO.getMpassword().equals("1111")
                || passwordEncoder.matches("1111", memberSecurityDTO.getMpassword()))){
            log.info("Should Change Password");

            log.info("Redirect to Member Modify");
            response.sendRedirect("/toto/member/modify");
            return;
        }else if(savedRequest != null){
            String targetUrl = savedRequest.getRedirectUrl();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
        {
            log.info("Authentication success go to main");
            response.sendRedirect("/toto/main");
        }
    }
}
