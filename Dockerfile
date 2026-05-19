# -- build stage --
# 這裡原本的 maven 映像檔可以保留，因為它只負責編譯，不負責運行爬蟲
FROM maven:3.9.12-eclipse-temurin-21 AS build

COPY pom.xml /root
COPY src /root/src

WORKDIR /root
RUN mvn clean package -Dmaven.test.skip=true

# -- package stage --
# 💡 修改點 1：將 alpine 改為 jammy (Ubuntu 22.04)，換掉底層網路特徵
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# 複製編譯好的 jar 檔
COPY --from=build /root/target/*-SNAPSHOT.jar app.jar

# 💡 修改點 2：Ubuntu 支援更標準的 bash，並保留你原本處理動態 PORT 的優秀邏輯
ENTRYPOINT ["bash", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]