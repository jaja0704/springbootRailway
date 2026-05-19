package org.example;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeelungSightsCrawler {

    private static final Logger logger = LoggerFactory.getLogger(KeelungSightsCrawler.class);

    public Sight[] getItems(String string) {

        Sight[] ResultSights = null;

        try {
            String htmlPage = Jsoup.connect("https://www.travelking.com.tw/tourguide/taiwan/keelungcity/")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .referrer("https://www.google.com")
                .timeout(5000)
                .get().toString();
            Document doc = Jsoup.parse(htmlPage, "https://www.travelking.com.tw/tourguide/taiwan/keelungcity/");

            String selector = String.format("h4:contains(%s)", string);
            Element headElements = doc.selectFirst(selector);
            ArrayList<Sight> tempSights = new ArrayList<Sight>();


            System.out.println("--- 開始抓取 [" + headElements.text() + "] 下方的內容 ---");


            if (headElements != null) {
                Element targElement = headElements.nextElementSibling();

                if (targElement != null && targElement.tagName().equals("ul")) {
                    Elements links = targElement.select("li a");

                    for (Element link : links) {
                        String tempUrl = link.attr("abs:href");
                        System.out.println("網址: " + tempUrl);
                        Sight temp =SubPage(tempUrl);
                        temp.setZone(string);
                        tempSights.add(temp);
                    }
                }
            }
            ResultSights = tempSights.toArray(new Sight[0]);
            System.out.println(ResultSights);

        } catch (HttpStatusException e) {
            // 使用 logger 物件記錄錯誤，這不會讓 Spring Boot 崩潰，但能讓你查日誌時一目了然
            logger.error("【爬蟲失敗】目標網站封鎖了我們的連線 (403 Forbidden)！", e);
        } catch (IOException e) {
            logger.error("【網路異常】連線到旅遊王失敗！", e);
        } catch (Exception e) {
            logger.error("遭遇其他未知錯誤，但強制讓專案繼續啟動", e);
        }
        return ResultSights;
        // throw new UnsupportedOperationException("Unimplemented method 'getItems'");
    }

    public static Sight SubPage(String url){
        Sight tempSight = new Sight();

        try {
            String subHtmlPage = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .referrer("https://www.google.com")
                .timeout(5000)
                .get().toString();
            Document subDoc = Jsoup.parse(subHtmlPage, url);
            // String title = subDoc.title();

            Elements spanTags = subDoc.select("span strong");
            // System.out.println(spanTags.text());
            tempSight.setCategory(spanTags.text());


            Elements metaTags = subDoc.select("div#point_area meta");
            for (Element meta : metaTags) {
                String property = meta.attr("itemprop"); // 抓取 itemprop 屬性
                String content = meta.attr("content");   // 抓取 content 屬性

                // System.out.println("屬性: " + property + " | 內容: " + content);
                if("name".equals(property)){
                    tempSight.setSightName(content);
                }
                else if("image".equals(property)){
                    tempSight.setPhotoURL(content);
                }
                else if("description".equals(property)){
                    tempSight.setDescription(content);
                }
                else if("address".equals(property)){
                    tempSight.setAddress(content);
                }
                else{
                    continue;
                }
            }

        } catch (IOException e) {
            System.err.println("讀取內頁失敗: " + url + " 原因: " + e.getMessage());
            logger.error("【網路異常】連線到旅遊分頁失敗！", e);
        } catch (Exception e) {
            logger.error("遭遇其他未知錯誤，但強制讓專案繼續啟動", e);
        }

        // System.out.println(tempSight);
        return tempSight;
    }

}
