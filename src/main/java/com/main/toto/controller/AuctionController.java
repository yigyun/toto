package com.main.toto.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/toto/main")
public class AuctionController {

    @GetMapping
    public String mainGET(){
        log.info("main get............");
        return "toto/auction/main";
    }

}
