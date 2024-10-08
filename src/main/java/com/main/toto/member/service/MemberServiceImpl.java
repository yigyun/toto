package com.main.toto.member.service;

import com.main.toto.member.dto.member.MemberJoinDTO;
import com.main.toto.member.entity.member.Member;
import com.main.toto.member.entity.member.MemberRole;
import com.main.toto.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void join(MemberJoinDTO memberJoinDTO)  {


        String mid = memberJoinDTO.getMid();

        Member member = modelMapper.map(memberJoinDTO, Member.class);
        member.changePassword(passwordEncoder.encode(member.getMpassword()));
        member.addRole(MemberRole.USER);

        log.info("====================================");
        log.info(member);

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void changePw(String mpassword, String mid) {
        log.info("changePw............");
        memberRepository.updatePassword(mpassword, mid);
    }

    @Override
    public void delete(String mid) {
        log.info("delete............");
        memberRepository.deleteById(mid);
    }
}
