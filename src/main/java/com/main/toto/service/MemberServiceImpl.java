package com.main.toto.service;

import com.main.toto.domain.member.Member;
import com.main.toto.domain.member.MemberRole;
import com.main.toto.dto.member.MemberJoinDTO;
import com.main.toto.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {


        String mid = memberJoinDTO.getMid();

        boolean exist = memberRepository.existsById(mid);

        if(exist){
            throw new MidExistException();
        }

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
}
