package com.main.toto.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    @Profile("test")
    public CommandLineRunner testDataInitializer(DataInitializer dataInitializer) {
        // 빈 생성을 방지하기 위해 빈을 오버라이드하고, 빈 생성 시 아무 동작도 하지 않는 CommandLineRunner를 반환합니다.
        return args -> {};
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
