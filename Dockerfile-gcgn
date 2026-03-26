FROM eclipse-temurin:21-jdk
VOLUME /tmp
ADD /build/libs/*-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8300

ARG SPRING_PROFILE=dev

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=${SPRING_PROFILE}","-Duser.timezone=Asia/Seoul","-jar","/app.jar"]
