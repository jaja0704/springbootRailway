# === 第一階段：編譯環境 ===
FROM ubuntu:22.04 AS builder
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y openjdk-21-jdk maven && rm -rf /var/lib/apt-get/lists/*

WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# === 第二階段：運行環境（只保留執行需要的東西） ===
# 使用 Eclipse Temurin 官方基於 Ubuntu (jammy) 的 Runtime 映像檔
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app
# 從編譯階段把打包好的 jar 檔偷過來
COPY --from=builder /build/target/*.jar app.jar

# 雲端平台動態 Port 設定
ENV PORT 8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]