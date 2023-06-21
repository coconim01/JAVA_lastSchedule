package com.lastSchedule;

import com.lastSchedule.entity.RedDate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ApiExplorer {
    public static void main(String[] args) throws IOException {
        List<RedDate> redDates = makeRedDate();
        // redDates 사용...
    }

    public static List<RedDate> makeRedDate() {
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=H%2FpvVk4cDw%2BykrVPDR29tuhvoEkW6ygJKgaX2iyv8ugusT675mz2KLXmyz7UYSpq93CQLmgmpMNGzwQrLx09Fg%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100"));

            urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode(String.valueOf(LocalDate.now().getYear()), "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("solMonth","UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));

            int pageNo = 1; // 시작 페이지 번호
            int totalCount = 0; // 전체 아이템 수 초기화
            List<RedDate> redDateList = new ArrayList<>();

            while (true) {
                // 페이지 번호 설정
                urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(String.valueOf(pageNo), "UTF-8"));

                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");
                System.out.println("Response code: " + conn.getResponseCode());

                BufferedReader rd;
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();
                System.out.println(sb.toString());

                // XML 파싱
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new ByteArrayInputStream(sb.toString().getBytes()));
                NodeList itemList = document.getElementsByTagName("item");

                // 전체 아이템 수 가져오기 (첫 페이지에만 있는 totalCount 값)
                if (totalCount == 0) {
                    NodeList totalCountList = document.getElementsByTagName("totalCount");
                    totalCount = Integer.parseInt(totalCountList.item(0).getTextContent());
                }

                for (int i = 0; i < itemList.getLength(); i++) {
                    Node itemNode = itemList.item(i);
                    if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element itemElement = (Element) itemNode;

                        // <locdate> 값 가져오기
                        NodeList datetimeList = itemElement.getElementsByTagName("locdate");
                        String datetime = datetimeList.item(0).getTextContent();
                        LocalDate date = LocalDate.parse(datetime, DateTimeFormatter.ofPattern("yyyyMMdd"));

                        // <dateName> 값 가져오기
                        NodeList dateNameList = itemElement.getElementsByTagName("dateName");
                        String dateName = dateNameList.item(0).getTextContent();

                        System.out.println("Datetime: " + date);
                        System.out.println("DateName: " + dateName);

                        RedDate redDate = RedDate.createRedDate(date.atStartOfDay(), dateName);
                        System.out.println(redDate);
                        redDateList.add(redDate);
                    }
                }

                // 마지막 페이지인 경우 반복문 종료
                if (redDateList.size() >= totalCount) {
                    break;
                }

                // 다음 페이지로 이동
                pageNo++;
            }

            return redDateList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
