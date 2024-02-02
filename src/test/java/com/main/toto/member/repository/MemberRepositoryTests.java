package com.main.toto.member.repository;

import com.main.toto.member.entity.member.Member;
import com.main.toto.member.entity.member.MemberRole;
import com.main.toto.member.repository.MemberRepository;
import com.main.toto.member.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @DataJpaTest는 임베디드 방식으로 동작함.
 * 내가 원하는 방식으로 만들려면 @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 이렇게 추가해서
 * 내가 설정한 yml 대로 사용하게 함.
 */

@DataJpaTest
@ActiveProfiles("test")
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    @Transactional
    public void setup(){
//        autoincrementReset();
        entityManager.clear();
        dataSetup();
    }

    @DisplayName("회원 가입 테스트(리포지토리, 관리자)")
    @Test
    @Transactional
    public void join(){
        memberRepository.findById("member1").ifPresent(member -> {
            assertThat(member.getMid()).isEqualTo("member1");
        });
    }

    @DisplayName("회원 조회 테스트")
    @Test
    @Transactional(readOnly = true)
    public void getWithRolesTest(){
        log.info("read-------------------");
        Optional<Member> result = memberRepository.getWithRoles("member10");
        result.ifPresent(member -> {
            assertThat(member.getMid()).isEqualTo("member10");
            member.getRoleSet().forEach(role -> {
                log.info("Role : " + role);
            });
        });
    }

    @DisplayName("회원 이메일로 조회 테스트")
    @Test
    @Transactional(readOnly = true)
    public void findByEmailTest(){
        memberRepository.findByEmail("email1@aaa.com").ifPresent(member ->{
            assertThat(member.getEmail()).isEqualTo("email1@aaa.com");
        });
    }

    @DisplayName("회원 비밀번호 변경 테스트")
    @Test
    @Transactional
    public void changePw(){

        String password = "2222";
        String mid = "member1";
        memberRepository.updatePassword(password, mid);

        entityManager.flush();
        entityManager.clear();

        memberRepository.findById(mid).ifPresent(selectMember -> {
            assertThat(selectMember.getMpassword()).isEqualTo("2222");
        });
    }

    // 더티 체킹
    @DisplayName("회원 권한 변경 테스트")
    @Test
    @Transactional
    public void changeRole(){

        String mid = "member1";
        Member member = memberRepository.findById(mid).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 없습니다. mid=" + mid));
        member.addRole(MemberRole.ADMIN);

        entityManager.flush();
        entityManager.clear();

        memberRepository.findById(mid).ifPresent(selectMember -> {
            assertThat(selectMember.getRoleSet().size()).isEqualTo(2);
        });
    }
    
    @DisplayName("회원 삭제 테스트")
    @Test
    @Transactional
    public void delete(){
        String mid = "member1";
        memberRepository.deleteById(mid);

        entityManager.flush();
        entityManager.clear();

        memberRepository.findById(mid).ifPresent(selectMember -> {
            assertThat(selectMember).isNull();
        });
    }

    private void dataSetup(){
        IntStream.rangeClosed(1,10).forEach(i -> {
            Member member = Member.builder()
                    .mid("member" + i)
                    .mpassword(("1111"))
                    .email("email" + i + "@aaa.com")
                    .build();
            member.addRole(MemberRole.USER);

            if(i >= 8){
                member.addRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        });
    }
}
