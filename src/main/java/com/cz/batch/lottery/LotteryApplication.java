package com.cz.batch.lottery;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ConfigurableApplicationContext;

@ConditionalOnMissingBean(value = DefaultBatchConfiguration.class, annotation = EnableBatchProcessing.class)
@SpringBootApplication
public class LotteryApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(LotteryApplication.class, args);
		System.exit(SpringApplication.exit(context));
	}
}
