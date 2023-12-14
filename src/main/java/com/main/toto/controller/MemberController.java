package com.main.toto.controller;

import com.main.toto.dto.member.MemberJoinDTO;
import com.main.toto.dto.member.MemberPwModifyDTO;
import com.main.toto.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/toto/member")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/login")
    public String loginGET(String error, String logout, Principal principal){
        log.info("error: " + error);
        log.info("logout: " + logout);


        if(principal != null) {
            //이미 로그인한 경우 메인 페이지로 리다이렉트 시킨다.
            return "redirect:/toto/main";
        }
        if(logout != null){
            log.info("user logout............");
        }
        return "toto/member/login";
    }

    @GetMapping("/join")
    public String joinGET(Model model){
        log.info("join get............");
        model.addAttribute("member", new MemberJoinDTO());
        return "toto/member/join";
    }

    // 링크 문제임 와서 고칠것.

    @PostMapping("/join")
    public String joinPOST(@Validated @ModelAttribute("member") MemberJoinDTO memberJoinDTO,BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){
        log.info("join post............");
        log.info("join 내용" + memberJoinDTO);

        if(bindingResult.hasErrors()){
            log.info("bindingResult: " + bindingResult);
            return "toto/member/join";
        }

        try{
            memberService.join(memberJoinDTO);
        } catch (MemberService.MidExistException e){
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/toto/member/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/toto/member/login";
    }

    @GetMapping("/main")
    public String mainGET(){
        log.info("main get............");
        return "Main이다 임마";
    }

    @GetMapping("/modify")
    public String pwModifyGET(Model model){
        log.info("pwModify get............");
        model.addAttribute("pwModify", new MemberPwModifyDTO());
        return "toto/member/modify";
    }

    @PostMapping("/modify")
    public String pwModifyPOST(@Validated @ModelAttribute("pwModify") MemberPwModifyDTO memberPwModifyDTO,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes
                                , Principal principal){
        log.info("pwModify post............");
        if(bindingResult.hasErrors()){
            log.info("bindingResult: " + bindingResult);
            return "toto/member/modify";
        }

        String mid = principal.getName();
        memberService.changePw(memberPwModifyDTO.getMpassword(), mid);

        return "redirect:/toto/main";
    }
}
