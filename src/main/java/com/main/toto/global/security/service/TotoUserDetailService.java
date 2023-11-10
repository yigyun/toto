package com.main.toto.global.security.service;

import com.main.toto.domain.member.Member;
import com.main.toto.global.security.dto.MemberSecurityDTO;
import com.main.toto.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 일반 form 로그인 처리
 */

@Service
@RequiredArgsConstructor
@Log4j2
public class TotoUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("UserDetailsService loadUserByUsername: " + username);
        Optional<Member> result = memberRepository.getWithRoles(username);

        log.info("이 문장이 안나오면 getWithRoles 문제");
        if(result.isEmpty()){
            throw new UsernameNotFoundException("username NOT EXIST");
        }

        Member member = result.get();
        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getMid(),
                member.getMpassword(),
                member.getEmail(),
                member.isDel(),
                false,
                member.getRoleSet()
                        .stream().map(memberRole -> new SimpleGrantedAuthority(
                                "ROLE_" + memberRole.name())).collect(Collectors.toList())
        );

        return memberSecurityDTO;
    }
}
