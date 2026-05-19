package org.example.runner;

import java.util.Arrays;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.example.KeelungSightsCrawler;
import org.example.repository.SightRepository;
import org.example.Sight;



@Component
public class tourStarter{

    private static final Logger log = LoggerFactory.getLogger(tourStarter.class);

    @Autowired
    private ApplicationArguments args;
    
    @Autowired
    private SightRepository sightRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void run(){
        String [] districts = {"中山區", "信義區", "仁愛區", "中正區", "安樂區", "七堵區", "暖暖區"};
        System.out.println("=== 系統啟動中：執行 tourStarter ===");

        try{
            sightRepository.deleteAll();

            if (args.containsOption("init-db")) {
                System.out.println("偵測到 init-db 參數，正在初始化資料庫...");
            }

            KeelungSightsCrawler crawler = new KeelungSightsCrawler();

            for (String district : districts) {
                Sight [] sights = crawler.getItems(district);
                sightRepository.insert(Arrays.asList(sights));
            }

            System.out.println("=== tourStarter 執行完畢 ===");
        } catch (RuntimeException e) {
            log.error("TourStarter 執行期間發生錯誤（可能被目標網站封鎖或網路異常），但網站仍會保持正常運作。", e);
        }
    }
}
