FROM openjdk:8-jdk-alpine
LABEL maintainer="manojsamaraweera@gmail.com"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/usermanager-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} usermanager.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/usermanager.jar"]
