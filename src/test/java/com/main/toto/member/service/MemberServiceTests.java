package com.main.toto.member.service;

import com.main.toto.member.dto.member.MemberJoinDTO;
import com.main.toto.member.repository.MemberRepository;
import com.main.toto.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class MemberServiceTests {

    @SpyBean
    private MemberService memberService;

    @SpyBean
    private MemberRepository memberRepository;

    @DisplayName("회원 가입 테스트(서비스)")
    @Test
    public void join()throws Exception{
        IntStream.rangeClosed(1,10).forEach(i -> {
            MemberJoinDTO member = new MemberJoinDTO();
            member.setMid("member" + i);
            member.setMpassword("1111");
            member.setEmail("email" + i + "@aaa.com");
            memberService.join(member);
        });
    }

    @DisplayName("비밀번호 변경 테스트")
    @Test
    public void changePw()throws Exception{
        //given
        MemberJoinDTO member = new MemberJoinDTO();
        member.setMid("member1");
        member.setMpassword("1111");
        member.setEmail("email"+ 1 + "@aaa.com");
        memberService.join(member);
        // when
        memberService.changePw("2222", "member1");
        //then
        memberRepository.findById("member1").ifPresent(selectMember -> {
            assertThat(selectMember.getMpassword()).isEqualTo("2222");
        });
    }
}
