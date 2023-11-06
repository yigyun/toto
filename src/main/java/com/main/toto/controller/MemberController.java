package com.main.toto.controller;

import com.main.toto.dto.MemberJoinDTO;
import com.main.toto.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/toto/member")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/login")
    public String loginGET(String error, String logout){
        log.info("error: " + error);
        log.info("logout: " + logout);

        if(logout != null){
            log.info("user logout............");
        }
        return "toto/member/login";
    }

    @GetMapping("/join")
    public void joinGET(){
        log.info("join get............");
    }

    @PostMapping("/join")
    public String joinPOST(@Valid MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes){
        log.info("join post............");
        log.info(memberJoinDTO);

        try{
            memberService.join(memberJoinDTO);
        } catch (MemberService.MidExistException e){
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/toto/member/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/toto/member/main";
    }

    @GetMapping("/main")
    public void mainGET(){
        log.info("main get............");
    }
}
