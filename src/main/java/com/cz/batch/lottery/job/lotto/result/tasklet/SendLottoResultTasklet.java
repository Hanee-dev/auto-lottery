package com.cz.batch.lottery.job.lotto.result.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * 로또 결과 전송
 */
public class SendLottoResultTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();

        ArrayList<String> listNoLotto = (ArrayList<String>) jobExecutionContext.get("listNoLotto");
        ArrayList<String[]> listApplyLotto = (ArrayList<String[]>) jobExecutionContext.get("listApplyLotto");

        // 1. 금주 당첨번호
        System.out.println(listNoLotto);
        // 2. 신청번호
        Objects.requireNonNull(listApplyLotto).stream().map(Arrays::toString).forEach(System.out::println);

        // TODO 당첨번호 검증 및 전송

        return RepeatStatus.FINISHED;
    }
}
