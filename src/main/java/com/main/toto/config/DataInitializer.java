package com.main.toto.config;

import com.main.toto.domain.board.Board;
import com.main.toto.domain.board.BoardCategory;
import com.main.toto.domain.board.BoardImage;
import com.main.toto.domain.member.Member;
import com.main.toto.dto.board.BoardDTO;
import com.main.toto.dto.member.MemberJoinDTO;
import com.main.toto.service.BoardService;
import com.main.toto.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final MemberService memberService;
    private final BoardService boardService;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // 초기 Member 객체 생성
            MemberJoinDTO memberJoinDTO = new MemberJoinDTO();
            memberJoinDTO.setMid("test");
            memberJoinDTO.setMpassword("test");
            memberJoinDTO.setEmail("test@naver.com");
            // member 객체의 필드를 설정합니다.
            memberService.join(memberJoinDTO);

            long count = 1L;
            // 각 카테고리별 Board 데이터 생성
            for (BoardCategory category : BoardCategory.values()) {

                BoardImage boardImage = BoardImage.builder()
                        .uuid("o" + count)
                        .fileName("o" + count + ".jpg")
                        .build();

                BoardDTO boardDTO = BoardDTO.builder()
                        .title(category.getDescription() + " 게시글")
                        .bno(count++)
                        .content(category.getDescription() + " 게시글 내용")
                        .writer("test" )
                        .bookMarkCount(1L)
                        .boardCategory(category)
                        .fileNames(Arrays.asList(boardImage.getUuid() + "_" + boardImage.getFileName()))
                        .price(5000L)
                        .build();
                // board 객체의 필드를 설정합니다.

                boardService.register(boardDTO);
            }
        };
    }
}
