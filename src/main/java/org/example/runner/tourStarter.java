package org.example.runner;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import org.example.KeelungSightsCrawler;
import org.example.repository.SightRepository;
import org.example.Sight;

@Component
public class tourStarter implements ApplicationRunner {


    
    @Autowired
    private SightRepository sightRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String [] districts = {"中山區", "信義區", "仁愛區", "中正區", "安樂區", "七堵區", "暖暖區"};
        System.out.println("=== 系統啟動中：執行 tourStarter ===");
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
    }
}
