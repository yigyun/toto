package com.main.toto.bookMark.controller;

import com.main.toto.bookMark.dto.bookMark.BookMarkDTO;
import com.main.toto.bookMark.service.BookMarkService;
import com.main.toto.bookMark.service.BookMarkServiceImpl;
import com.main.toto.config.ControllerTestConfig;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@ActiveProfiles("test")
class BookMarkControllerTest extends ControllerTestConfig {

    @SpyBean
    private BookMarkServiceImpl bookMarkService;

    @BeforeAll
    static void setUp() {
        BookMarkDTO bookMarkDTO = new BookMarkDTO();
        bookMarkDTO.setBno(1L);
        bookMarkDTO.setMid("testUser");
    }

    @AfterEach
    void tearDown(){
        SecurityContextHolder.clearContext();
    }

    @DisplayName("북마크 추가")
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void addBookMark() throws Exception {
        doNothing().when(bookMarkService).addBookMark(1L, "testUser");

        mvc.perform(post("/bookMark/add")
                .contentType("application/json")
                .content("{\"bno\":1,\"mid\":\"testUser\"}"))
                .andExpect(status().isOk());
    }

    @DisplayName("북마크 추가 실패 BAD_REQUEST")
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void addBookMarkFail() throws Exception {
        mvc.perform(post("/bookMark/add")
                .contentType("application/json")
                .content("{\"bno\":-1,\"mid\":\"testUser\"}"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("북마크 삭제")
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void deleteBookMark() throws Exception{
        doNothing().when(bookMarkService).deleteBookMark(1L, "testUser");
        mvc.perform(delete("/bookMark/delete")
                .contentType("application/json")
                .content("{\"bno\":1,\"mid\":\"testUser\"}"))
                .andExpect(status().isOk());
    }

    @DisplayName("북마크 삭제 실패 BAD_REQUEST")
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void deleteBookMarkFail() throws Exception {
        mvc.perform(delete("/bookMark/delete")
                .contentType("application/json")
                .content("{\"bno\":-1,\"mid\":\"testUser\"}"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("북마크 Service 에서 EntityNotFoundException 발생시 처리")
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void handleEntityNotFoundException() throws Exception{
        ResultActions resultActions = mvc.perform(post("/bookMark/add")
                .contentType("application/json")
                .content("{\"bno\":1,\"mid\":\"testUser\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Member not found: testUser"));
    }

    @DisplayName("북마크 Service 에서 IllegalArgumentException 발생시 처리")
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void handleIllegalArgumentException() throws Exception{
        doThrow(new IllegalArgumentException("Invalid request.")).when(bookMarkService).addBookMark(anyLong(), anyString());

        mvc.perform(post("/bookMark/add")
                        .contentType("application/json;charset=UTF-8")
                        .content("{\"bno\":1,\"mid\":\"testUser\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Invalid request.", new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8)));
    }
}