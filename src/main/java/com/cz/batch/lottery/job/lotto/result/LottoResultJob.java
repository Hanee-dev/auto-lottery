package com.cz.batch.lottery.job.lotto.result;

import com.cz.batch.lottery.job.lotto.result.tasklet.LottoResultTasklet;
import com.cz.batch.lottery.job.lotto.result.tasklet.SendLottoResultTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class LottoResultJob {

	public static final String JOB_NAME = "lottoResult";

	@Bean(name = JOB_NAME)
	public Job job(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
		return new JobBuilder(JOB_NAME, jobRepository)
				.start(this.lottoResultStep(jobRepository, platformTransactionManager))
				.next(this.sendResultStep(jobRepository, platformTransactionManager))
				.build();
	}

	@Bean(name = JOB_NAME + "_lottoResultStep")
	public Step lottoResultStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
		return new StepBuilder(JOB_NAME + "_lottoResultStep", jobRepository)
				.tasklet(new LottoResultTasklet(), platformTransactionManager).build();
	}

	@Bean(name = JOB_NAME + "_sendResultStep")
	public Step sendResultStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
		return new StepBuilder(JOB_NAME + "_sendResultStep", jobRepository)
				.tasklet(new SendLottoResultTasklet(), platformTransactionManager).build();
	}
}
