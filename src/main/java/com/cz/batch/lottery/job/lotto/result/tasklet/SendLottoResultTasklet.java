package com.cz.batch.lottery.job.lotto.result.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * 로또 결과 전송
 */
public class SendLottoResultTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("로또 결과 전송");

        return RepeatStatus.FINISHED;
    }
}
