# -- build stage --
FROM maven:3.9.12-eclipse-temurin-21 AS build

COPY pom.xml /root
COPY src /root/src

WORKDIR /root
RUN mvn clean package -Dmaven.test.skip=true

# -- package stage --
FROM eclipse-temurin:21-jre-alpine

# 解法 2：為了避免複製到 plain.jar 導致失敗，先建一個目錄存放，然後在執行時處理
# 這裡用一個小技巧，先將 target 整個複製過來，確保執行檔存在
WORKDIR /app
# 避免複製到 -plain.jar，假設一般 jar 的命名通常不包含 plain
COPY --from=build /root/target/*-SNAPSHOT.jar app.jar

# 解法 1：使用 ENTRYPOINT 並結合 shell 讀取 Railway 提供的環境變數 PORT
# 若本地測試沒有 PORT 變數，預設就使用 8080
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]