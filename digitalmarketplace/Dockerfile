FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} digitalmarketplace-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/digitalmarketplace-0.0.1-SNAPSHOT.jar"]
   
