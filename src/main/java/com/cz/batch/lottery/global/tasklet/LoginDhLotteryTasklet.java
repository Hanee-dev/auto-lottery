package com.cz.batch.lottery.global.tasklet;

import com.cz.batch.lottery.global.config.PlaywrightConfig;
import com.cz.batch.lottery.global.dto.DhUserDto;
import com.microsoft.playwright.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

/**
 * 동행복권 로그인
 */
@Component
@RequiredArgsConstructor
public class LoginDhLotteryTasklet implements Tasklet {
    private final DhUserDto dhUserDto;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            Page page = PlaywrightConfig.getInstance().getPage();

            // 1. 페이지 접근 및 로그인
            page.navigate("https://dhlottery.co.kr/user.do?method=login");

            page.click("[placeholder=\"아이디\"]");
            page.fill("[placeholder=\"아이디\"]", dhUserDto.getId());
            page.press("[placeholder=\"아이디\"]", "Tab");
            page.fill("[placeholder=\"비밀번호\"]", dhUserDto.getPw());
            page.press("[placeholder=\"비밀번호\"]", "Tab");

            page.locator("form[name='jform'] >> text=로그인").press("Enter");
        } catch (Exception e) {
            contribution.setExitStatus(ExitStatus.FAILED);
        }

        return RepeatStatus.FINISHED;
    }
}
