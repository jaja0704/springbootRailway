package org.example.runner;

import org.example.KeelungSightsCrawler;
import org.example.Sight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import org.example.repository.SightRepository;

@Service
public class CrawlerService {

    private static final Logger log = LoggerFactory.getLogger(CrawlerService.class);

    @Autowired
    private SightRepository sightRepository;

    @Autowired
    private ApplicationArguments args;


    @Async
    public void startBackgroundCrawling() {
        log.info("【背景執行緒】開始平行執行爬蟲任務...");
        String [] districts = {"中山區", "信義區", "仁愛區", "中正區", "安樂區", "七堵區", "暖暖區"};
//        System.out.println("=== 系統啟動中：執行 tourStarter ===");

        try {
            sightRepository.deleteAll();

            if (args.containsOption("init-db")) {
                System.out.println("偵測到 init-db 參數，正在初始化資料庫...");
            }

            KeelungSightsCrawler crawler = new KeelungSightsCrawler();

            for (String district : districts) {
                Sight[] sights = crawler.getItems(district);
                sightRepository.insert(Arrays.asList(sights));
            }

            System.out.println("=== startBackgroundCrawling 執行完畢 ===");
        } catch (Exception e) {
            log.error("爬蟲失敗", e);
        }
    }
}
