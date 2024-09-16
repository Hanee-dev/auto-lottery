package com.cz.batch.lottery.job.lotto.result.tasklet;

import com.cz.batch.lottery.global.config.PlaywrightConfig;
import com.microsoft.playwright.Page;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * 로또 결과 추출
 */
public class LottoResultTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("로또 결과 추출");

        PlaywrightConfig config = PlaywrightConfig.getInstance();
        Page page = config.getPlaywrightPage();


        return RepeatStatus.FINISHED;
    }
}
