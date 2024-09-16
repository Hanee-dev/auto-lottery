package com.cz.batch.lottery.global.config;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import jakarta.annotation.PreDestroy;


public class PlaywrightConfig {
    private static PlaywrightConfig playwrightConfig;

    private Playwright playwright;
    private Browser browser;
    private Page page;

    private PlaywrightConfig() {
        try {
            playwright = Playwright.create();
//            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            browser = playwright.chromium().launch();
            page = browser.newPage();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Playwright", e);
        }
    }

    public static PlaywrightConfig getInstance() {
        if (playwrightConfig == null)
            playwrightConfig = new PlaywrightConfig();

        return playwrightConfig;
    }

    public Page getPlaywrightPage() {
        return page;
    }

    @PreDestroy
    public void closePlaywrightResources() {
        if (page != null) {
            page.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}