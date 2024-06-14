package com.demo.service;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

@Service
public class scrapingService {

    private static final String URL = "https://www.gocamping.or.kr/zboard/list.do?lmCode=campSafetyVod";
    private static final String DRIVER_PATH = "/path/to/chromedriver"; // 크롬 드라이버 경로 설정

    public void scrapeData() throws IOException {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        WebDriver driver = new ChromeDriver();
        driver.get(URL);

        // 페이지 로드 대기
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Document doc = Jsoup.parse(driver.getPageSource());
        Elements posts = doc.select("div.board_list_box");

        for (Element post : posts) {
            String title = post.select("a.list_subject").text();
            Element videoTag = post.selectFirst("a[href*=video]"); // 비디오 링크 찾기

            if (videoTag != null) {
                String videoUrl = videoTag.attr("href");
                System.out.println("Title: " + title + ", Video URL: " + videoUrl);
            }
        }
        driver.quit();
    }
}
