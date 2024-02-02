package com.main.toto.config;

import com.main.toto.member.controller.MemberController;
import com.main.toto.member.service.MemberService;
import com.main.toto.member.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 컨트롤러는 보통 Mock를 활용해서 테스트를 진행한다.
 * @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)을 사용하면 컨트롤러에 대한 테스트이다.
 * 왜냐하면 서블릿 컨테이너를 시작하지 않고 테스틀 진행시킨다.
 *반면에 @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)를 사용하면  실제 서블릿 환경이 제공된다.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class ControllerTestConfig {

    @Autowired
    protected MockMvc mvc;

    @SpyBean
    protected MemberController memberController;

    @SpyBean
    protected MemberServiceImpl memberService;

}
