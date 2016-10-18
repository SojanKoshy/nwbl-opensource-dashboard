package com.huawei.nwbl.opensource.dashboard.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Gerrit home page scraper
 */
@Service
public class GerritScraperService {
    static final String BASE_URL = "https://gerrit.onosproject.org";
    final Logger log = getLogger(getClass());
    private final WebClient webClient = new WebClient(BrowserVersion.CHROME);
    String url;
    HtmlPage page;

    public void setUrl(String url) {
        this.url = url;
    }

    public void scrape() {
        loadPage();
        log.info("Page as xml\n{}", page.asXml());
        log.info("Page as text\n{}", page.asText());
    }

    void loadPage() {
        try {
            log.info("Loading page {}", url);
            long lStartTime = new Date().getTime();
            page = webClient.getPage(url);
            long lEndTime = new Date().getTime();
            long elapsedTime = (lEndTime - lStartTime) / 1000;

            if (log.isDebugEnabled()) {
                log.debug("Page as xml\n{}", page.asXml());
                log.debug("Page as text\n{}", page.asText());
            }

            log.info("Page loaded in {} sec", elapsedTime);
        } catch (IOException e) {
            log.error("Error in loading page {}", url);
        }
        JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();
        while (manager.getJobCount() > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("Error in js while loading page {}", url);
            }
        }
    }
}
