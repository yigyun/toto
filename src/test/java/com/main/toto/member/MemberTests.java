package com.main.toto.member;

import com.main.toto.domain.member.Member;
import com.main.toto.domain.member.MemberRole;
import com.main.toto.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void join(){
        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder()
                    .mid("member" + i)
                    .mpassword(passwordEncoder.encode("1111"))
                    .email("email" + i + "@aaa.com")
                    .build();
            member.addRole(MemberRole.USER);

            if(i >= 90){
                member.addRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        });
    }

    @Test
    public void read(){
        log.info("join-------------------");
        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder()
                    .mid("member" + i)
                    .mpassword(passwordEncoder.encode("1111"))
                    .email("email" + i + "@aaa.com")
                    .build();
            member.addRole(MemberRole.USER);

            if(i >= 90){
                member.addRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        });

        log.info("read-------------------");
        Optional<Member> result = memberRepository.getWithRoles("member100");

        Member member = result.get();

        log.info(member);

        member.getRoleSet().forEach(memberRole -> log.info(memberRole.name()));
    }
}
