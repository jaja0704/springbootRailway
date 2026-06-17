/*
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
public class TourStarter {

    private static final Logger log = LoggerFactory.getLogger(TourStarter.class);
//    @Autowired
//    private ApplicationArguments args;
//
//    @Autowired
//    private SightRepository sightRepository;

//    @EventListener(ApplicationReadyEvent.class)
//    public void run(){
//        String [] districts = {"中山區", "信義區", "仁愛區", "中正區", "安樂區", "七堵區", "暖暖區"};
//        System.out.println("=== 系統啟動中：執行 tourStarter ===");
//
//        try{
//            sightRepository.deleteAll();
//
//            if (args.containsOption("init-db")) {
//                System.out.println("偵測到 init-db 參數，正在初始化資料庫...");
//            }
//
//            KeelungSightsCrawler crawler = new KeelungSightsCrawler();
//
//            for (String district : districts) {
//                Sight [] sights = crawler.getItems(district);
//                sightRepository.insert(Arrays.asList(sights));
//            }
//
//            System.out.println("=== tourStarter 執行完畢 ===");
//        } catch (RuntimeException e) {
//            log.error("TourStarter 執行期間發生錯誤（可能被目標網站封鎖或網路異常），但網站仍會保持正常運作。", e);
//        }
//    }

    @Autowired
    private CrawlerService crawlerService;

    // 💡 決定時機：當網站成功啟動、Tomcat 開門後觸發
    @EventListener(ApplicationReadyEvent.class)
    public void initTourData() {
        log.info("【主執行緒】網站已 Live！準備呼叫背景爬蟲...");

        // 💡 下達指令！因為對方有 @Async，所以這行程式碼會「瞬間執行完畢」，不會卡住
        crawlerService.startBackgroundCrawling();

        log.info("【主執行緒】指令已下達，主執行緒收工，繼續去服務一般網頁使用者！");
    }

}
*/
package org.example.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TourStarter implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(TourStarter.class);

    @Autowired
    private CrawlerService crawlerService;

    // 💡 實作 ApplicationRunner 必須 Override 的 run 方法
    // 時機：Spring Boot 初始化完成，準備開始接收 Request 之前觸發
    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 如果你要結合原本註解掉的邏輯，現在可以直接這樣寫：
        // if (args.containsOption("init-db")) {
        //     log.info("偵測到 init-db 參數，執行特定初始化...");
        // }

        log.info("【主執行緒】網站啟動中！準備呼叫背景爬蟲...");

        // 💡 下達指令！因為對方有 @Async，所以這行程式碼會「瞬間執行完畢」，不會卡住
        crawlerService.startBackgroundCrawling();

        log.info("【主執行緒】指令已下達，主執行緒收工，繼續去服務一般網頁使用者！");
    }
}