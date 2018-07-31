FROM openjdk:8-jdk-alpine
ADD target/noteRestServices.jar ws_noteRestServices_sf.jar
EXPOSE 8084
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap  -XX:MaxRAMFraction=1 -XshowSettings:vm "
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar ws_noteRestServices_sf.jar" ]