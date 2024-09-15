package com.cz.batch.lottery.job.lotto.apply;

import com.cz.batch.lottery.job.lotto.apply.tasklet.LottoApplyTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class LottoApplyJob {

	public static final String JOB_NAME = "lottoApply";

	@Bean(name = JOB_NAME)
	public Job job(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
		return new JobBuilder(JOB_NAME, jobRepository)
				.start(this.lottoApplyStep(jobRepository, platformTransactionManager))
				.build();
	}

	@Bean(name = JOB_NAME + "_lottoApplyStep")
	public Step lottoApplyStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
		return new StepBuilder(JOB_NAME + "_lottoApplyStep", jobRepository)
				.tasklet(new LottoApplyTasklet(), platformTransactionManager).build();
	}
}
