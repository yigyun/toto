package com.main.toto.member.dto.member.UniqueMid;

import com.main.toto.member.repository.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueMidValidator implements ConstraintValidator<UniqueMid, String> {

    private final MemberRepository memberRepository;

    public UniqueMidValidator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void initialize(UniqueMid constraintAnnotation) {
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(String mid, ConstraintValidatorContext context) {
        return mid != null && !memberRepository.existsById(mid);
    }
}
