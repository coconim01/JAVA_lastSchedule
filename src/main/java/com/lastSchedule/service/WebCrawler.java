package com.lastSchedule.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class WebCrawler {
    public static void main(String[] args) {
        String noticeUrl = "https://www.gmhs.hs.kr/wah/main/bbs/board/list.htm?menuCode=130"; // 공지사항 URL을 입력해주세요

        try {
            Document document = Jsoup.connect(noticeUrl).get();

            // 공지사항 게시판 크롤링
            Elements noticeElements = document.select(".notice-board .notice-item");

            // 공지사항 출력
            for (Element element : noticeElements) {
                String title = element.select(".title").text();
                String link = element.select("a").attr("abs:href");
                System.out.println("제목: " + title);
                System.out.println("링크: " + link);
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
