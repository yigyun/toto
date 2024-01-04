package com.main.toto.config;


import com.main.toto.domain.board.AuctionStatus;
import com.main.toto.domain.board.Board;
import com.main.toto.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final BoardRepository boardRepository;

    @Bean
    public Step updateBoardStatusStep() {
        return stepBuilderFactory.get("updateBoardStatusStep")
                .tasklet((contribution, chunkContext) -> {
                    LocalDateTime now = LocalDateTime.now();
                    Duration duration = Duration.ofHours(24);
                    LocalDateTime threshold = now.minus(duration);

                    List<Board> expiredBoards = boardRepository.findAllByRegDateBefore(threshold);
                    expiredBoards.forEach(board -> board.changeStatus(AuctionStatus.END));
                    boardRepository.saveAll(expiredBoards);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Job updateBoardStatusJob() {
        return jobBuilderFactory.get("updateBoardStatusJob")
                .incrementer(new RunIdIncrementer())
                .flow(updateBoardStatusStep())
                .end()
                .build();
    }
}
