package org.example.controller;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.example.KeelungSightsCrawler;
import org.example.Sight;
import org.example.repository.*;



@RestController
public class SightController {
    // private static final Map<String, Sight> SightMap = new HashMap<>();

    // 注入 MongoTemplate，這是操作 MongoDB 的萬用瑞士刀
    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/test-db")
    public String testDatabaseConnection() {
        try {
            // 1. 獲取目前連線的資料庫名稱
            String dbName = mongoTemplate.getDb().getName();
            
            // 2. 獲取該資料庫底下所有的 Collection 名稱
            Set<String> collections = mongoTemplate.getCollectionNames();
            
            // 3. 檢查指定的 collection ("sights") 是否存在
            boolean hasSights = mongoTemplate.collectionExists("sights");

            // 4. 將結果組合起來回傳給畫面
            return String.format(
                "✅ 連線成功！<br>" +
                "📍 目前連線的資料庫：%s <br>" +
                "📁 包含的 Collections：%s <br>" +
                "🎯 是否包含 'sights'：%s", 
                dbName, collections, hasSights
            );

        } catch (Exception e) {
            // 如果連線失敗，會捕捉到異常並顯示錯誤訊息
            return "❌ 連線失敗：" + e.getMessage();
        }
    }


    @Autowired
    private SightRepository sightRepository;

    @GetMapping("/sight")
    public ResponseEntity<Sight[]> getProducts(
            @RequestParam(value = "zone", required = false, defaultValue = "") String keyword)throws Exception {

            KeelungSightsCrawler crawler = new KeelungSightsCrawler();
            Sight [] sights = crawler.getItems(keyword); 
            
            // for (Sight s: sights) {

            //     System.out.println(s);
            // }


        return ResponseEntity.status(HttpStatus.OK).body(sights);
    }

    
    @GetMapping("/sights")
    public ResponseEntity<Sight[]> getSightDB(
            @RequestParam(value = "zone", required = false, defaultValue = "") String keyword)throws Exception {

            Sight [] sights = sightRepository.findByZone(keyword);

        return ResponseEntity.status(HttpStatus.OK).body(sights);
    }

    @GetMapping("/sights/{id}")
    public ResponseEntity<Sight> getStudent(@PathVariable String id) {
        Optional<Sight> studentOp = sightRepository.findById(id);
        return studentOp.isPresent()
                ? ResponseEntity.ok(studentOp.get())
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/sights/ids")
    public ResponseEntity<List<Sight>> getStudents(@RequestParam List<String> idList) {
        List<Sight> sights = sightRepository.findAllById(idList);
        return ResponseEntity.ok(sights);
    }

   @PostMapping("/sights/reset")
    public ResponseEntity<Void> resetSights() {
        Sight s1 = new Sight();
        s1.setSightName("測試景點 A");
        s1.setZone("zone1");
        s1.setCategory("風景區");
        s1.setPhotoURL("https://via.placeholder.com/400x200");
        s1.setAddress("基隆市中正區北寧路367號");
        s1.setDescription("這是一個美麗的海景公園。");
        

        Sight s2 = new Sight();
        s2.setSightName("測試景點 2");
        s2.setZone("zone2");
        s2.setCategory("歷史古蹟");
        s2.setPhotoURL("https://via.placeholder.com/400x200");
        s2.setAddress("基隆市中正區建國街");
        s2.setDescription("百年歷史的要塞。");

        
        sightRepository.deleteAll();
        sightRepository.insert(Arrays.asList(s1, s2));
        return ResponseEntity.noContent().build();
    }

}
