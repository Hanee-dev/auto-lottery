package com.cz.batch.lottery.job.lotto.result.tasklet;

import com.cz.batch.lottery.global.config.PlaywrightConfig;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.RequestOptions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 로또 결과 추출
 */
public class LottoResultTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        PlaywrightConfig instance = PlaywrightConfig.getInstance();
        Page page = instance.getPage();
        Browser browser = instance.getBrowser();

        // 1. main 페이지 접근
        page.navigate("https://dhlottery.co.kr/common.do?method=main");
        page.waitForSelector("#numView span[id]");

        // 2. 금주 로또번호 추출
        ArrayList<String> listNoLotto = new ArrayList<String>();
        List<ElementHandle> elementHandles = page.querySelectorAll("#numView span[id]");
        elementHandles.forEach(elementHandle -> listNoLotto.add(elementHandle.innerHTML()));

        jobExecutionContext.put("listNoLotto", listNoLotto);

        // 3. 당첨결과 추출
        // 3-1. 구매 내역 화면 로딩을 위한 항목 설정
        // 3-1-1. 쿠키
        List<Cookie> cookies = page.context().cookies();
        BrowserContext newContext = browser.newContext();
        newContext.addCookies(cookies);

        // 3-1-2. 파라미터
        String nowDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 3-2. 구매 내역 화면 호출
        APIResponse response = page.request().post("https://dhlottery.co.kr/myPage.do"
                , RequestOptions.create()
                        .setData("searchStartDate=" + nowDate + "&searchEndDate=" + nowDate + "&winGrade=2")
                        .setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                        .setHeader("Accept-Language", "ko,en;q=0.9,ko-KR;q=0.8,en-US;q=0.7")
                        .setHeader("Cache-Control", "max-age=0")
                        .setHeader("Connection", "keep-alive")
                        .setHeader("Content-Type", "application/x-www-form-urlencoded")
                        .setHeader("Origin", "https://dhlottery.co.kr")
                        .setHeader("Referer", "https://dhlottery.co.kr/myPage.do?method=lottoBuyListView")
                        .setHeader("Sec-Fetch-Dest", "iframe")
                        .setHeader("Sec-Fetch-Mode", "navigate")
                        .setHeader("Sec-Fetch-Site", "same-origin")
                        .setHeader("Sec-Fetch-User", "?1")
                        .setHeader("Upgrade-Insecure-Requests", "1")
                        .setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                        .setHeader("sec-ch-ua-mobile", "?0")
                        .setQueryParam("method", "lottoBuyList")
        );

        // 3-3. 구매 내역 화면 파싱
        String htmlContent = new String(response.body(), Charset.forName("EUC-KR"));

        Document document = Jsoup.parse(htmlContent);
        Element linkElement = document.selectFirst("tbody > tr:nth-child(1) > td:nth-child(4) > a");
        if (null == linkElement){
            contribution.setExitStatus(ExitStatus.COMPLETED);
            return RepeatStatus.FINISHED;
        }

        String aTagHref = linkElement.attr("href");

        // 3-4. 결과 화면 호출
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(aTagHref);
        String[] detailInfo = new String[3];
        int i = 0;
        while (matcher.find() && i < 3) {
            detailInfo[i++] = matcher.group();
        }

        String detailUrl = "https://dhlottery.co.kr/myPage.do?method=lotto645Detail&orderNo=" + detailInfo[0] + "&barcode=" + detailInfo[1] + "&issueNo=" + detailInfo[2];
        page.navigate(detailUrl);

        // 3-5. 결과 추출
        List<ElementHandle> selectedNoLottoElemHandles = page.querySelectorAll("div.selected li");

        jobExecutionContext.put("listApplyLotto", selectedNoLottoElemHandles.stream()
                                                .map(handle -> handle.innerText().split("\n"))
                                                .collect(Collectors.toCollection(ArrayList::new)));

        return RepeatStatus.FINISHED;
    }
}
