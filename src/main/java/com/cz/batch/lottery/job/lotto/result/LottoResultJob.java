package com.cz.batch.lottery.job.lotto.result;

import com.cz.batch.lottery.global.dto.DhUserDto;
import com.cz.batch.lottery.global.tasklet.LoginDhLotteryTasklet;
import com.cz.batch.lottery.job.lotto.result.tasklet.LottoResultTasklet;
import com.cz.batch.lottery.job.lotto.result.tasklet.SendLottoResultTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class LottoResultJob {
	public static final String JOB_NAME = "lottoResult";

	private final DhUserDto dhUserDto;

	@Bean(name = JOB_NAME)
	public Job job(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
		return new JobBuilder(JOB_NAME, jobRepository)
				.start(this.loginDhLotteryStep(jobRepository, platformTransactionManager))
					.on(ExitStatus.FAILED.getExitCode())
					.end()
				.from(this.loginDhLotteryStep(jobRepository, platformTransactionManager))
					.on("*")
					.to(lottoResultStep(jobRepository, platformTransactionManager))
					.next(this.sendResultStep(jobRepository, platformTransactionManager))
					.on("*")
					.end()
				.end()
				.build();
	}

	// 로그인
	@Bean(name = JOB_NAME + "_loginDhLotteryStep")
	public Step loginDhLotteryStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
		return new StepBuilder(JOB_NAME + "_loginDhLotteryStep", jobRepository)
				.tasklet(new LoginDhLotteryTasklet(dhUserDto), platformTransactionManager).build();
	}

	// 결과 추출
	@Bean(name = JOB_NAME + "_lottoResultStep")
	public Step lottoResultStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
		return new StepBuilder(JOB_NAME + "_lottoResultStep", jobRepository)
				.tasklet(new LottoResultTasklet(), platformTransactionManager).build(); // 임시
	}

	// 결과 전송
	@Bean(name = JOB_NAME + "_sendResultStep")
	public Step sendResultStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
		return new StepBuilder(JOB_NAME + "_sendResultStep", jobRepository)
				.tasklet(new SendLottoResultTasklet(), platformTransactionManager).build();
	}
}
