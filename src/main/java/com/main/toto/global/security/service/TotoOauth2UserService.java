package com.main.toto.global.security.service;

import com.main.toto.global.security.dto.MemberSecurityDTO;
import com.main.toto.member.entity.member.Member;
import com.main.toto.member.entity.member.MemberRole;
import com.main.toto.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TotoOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();
        String email = null;
        String clientName = userRequest.getClientRegistration().getClientName();
        log.info("oauth2 user Name: " + clientName);

        switch (clientName){
            case "kakao":
                email = getKakaoEmail(attributes);
                break;
            case "Naver":
                email = getNaverEmail(attributes);
                break;
            case "google":
                email = getGoogleEmail(attributes);
                break;
        }

        return generateDTO(email, attributes);
    }

    private MemberSecurityDTO generateDTO(String email, Map<String, Object> attributes){

        Optional<Member> join = memberRepository.findByEmail(email);
        if(join.isEmpty()){
            Member newMember = Member.builder()
                    .mid(email)
                    .email(email)
                    .mpassword(passwordEncoder.encode("1111"))
                    .social(true)
                    .build();

            newMember.addRole(MemberRole.USER);
            memberRepository.save(newMember);

            log.info("email 여기 확인: " + email);

            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(email, "1111", email,
                    false, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            log.info("여기까지는 되는지 확인");

            memberSecurityDTO.setProps(attributes);

            return memberSecurityDTO;
        } else {
            Member member = join.get();
            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(member.getMid(), member.getMpassword(), member.getEmail(),
                        member.isDel(), member.isSocial(),
                        member.getRoleSet().stream().map(
                                memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name())
                        ).collect(Collectors.toList()));

            return memberSecurityDTO;
        }
    }

    private String getKakaoEmail(Map<String, Object> attributes){
        log.info("Kakao------------------");
        Object value = attributes.get("kakao_account");

        LinkedHashMap accountMap = (LinkedHashMap) value;

        String email = (String) accountMap.get("email");
        log.info("email..." + email);
        return email;
    }

    private String getNaverEmail(Map<String, Object> attributes){
        log.info("Naver------------------");

        Object value = attributes.get("response");

        log.info("네이버 value값 확인: " + value);

        LinkedHashMap accountMap = (LinkedHashMap) value;

        String email = (String) accountMap.get("email");
        log.info("email..." + email);

        return email;
    }

    private String getGoogleEmail(Map<String, Object> attributes){
        log.info("Google------------------");
        String email = attributes.get("email").toString();
        return email;
    }
}
