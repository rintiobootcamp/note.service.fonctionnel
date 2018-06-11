FROM ibrahim/alpine
ADD target/noteRestServices.jar ws_noteRestServices_sf.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","ws_noteRestServices_sf.jar"]