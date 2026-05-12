# -- build stage --
# 在 build 階段可以挑選 maven:3.9.12-eclipse-temurin-21 當作基底 image，他裡面已經預裝好 Maven 和 Java 21，讚！（所以挑 image 真的也是一門藝術，挑到對的 image 就可以省去很多自己安裝的步驟）
# 記得最後面一定要加 AS xxx，這樣才能使用 Multi-stage build 的功能
FROM maven:3.9.12-eclipse-temurin-21 AS build

# 拉取必要的 source code 和 pom.xml 進到 image 裡面（即是把 src 底下的所有程式複製到 image 的 /root/src 中，把 pom.xml 複製到 image 的 /root 裡）
# 至於如何知道要放在 /root 下而不是放在 /usr 下，下面會講
# 另外注意這裡的順序很重要，一定要按照這個順序寫， 才不會使得每次改 src 都要重新下載 pom.xml 中的所有依賴
COPY pom.xml /root
COPY src /root/src

# cd 到 /root 資料夾裡面，並且執行 mvn build Spring Boot
WORKDIR /root
RUN mvn clean package -Dmaven.test.skip=true

# -- package stage --
# 因為在執行階段其實只需要 jre 就好，不需要整個 jdk 都拉進來，所以這裡採用的是 eclipse-temurin:21-jre-alpine，可以讓 image size 變得更小，選 image 的藝術 again
FROM eclipse-temurin:21-jre-alpine

# 偷 build 階段生成好的 .jar 檔進到此 image 裡面，所以就是複製 build 階段的 /root/target/*.jar 過來，並且將他改名成 app.jar
COPY --from=build /root/target/*.jar app.jar

# 聲明要開啟 8080 port
EXPOSE 8080

# 指定運行此 package image 的指令
CMD ["java", "-jar", "app.jar"]