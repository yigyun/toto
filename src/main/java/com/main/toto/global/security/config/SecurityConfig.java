package com.main.toto.global.security.config;

import com.main.toto.global.security.handler.TotoSocialLoginSuccessHandler;
import com.main.toto.global.security.service.TotoUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * 기본은 form 형태의 로그인을 활용한다.
 * Oath2.0을 활용한 로그인 또한 활용한다. (구글, 네이버, 카카오)
 */

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final DataSource dataSource;
    private final TotoUserDetailService userDetailsService;

    @Bean
    public SecurityFilterChain filterchain(HttpSecurity http) throws Exception{

        http.formLogin().loginPage("/toto/member/login");

        http.csrf().disable();

        http.rememberMe()
                .key("sB2?f*.BDGC003GT2")
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService)
                .tokenValiditySeconds(60 * 60 * 24 * 7); // 7일

        http.oauth2Login()
                .loginPage("/toto/member/login")
                .successHandler(authenticationSuccessHandler());

        return http.build();
    }

    // 정적 자원 처리
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("webSecurityCustomizer............");
        return (web) -> {
            web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        };
    }

    /**
     * 토큰을 브라우저, 서버에 저장하는 2가지 방법이 존재함. 그 중 후자
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new TotoSocialLoginSuccessHandler(passwordEncoder());
    }
}
