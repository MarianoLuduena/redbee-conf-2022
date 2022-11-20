FROM eclipse-temurin:11-jdk-alpine as build-env
RUN mkdir -p /opt/app
COPY . /opt/app
WORKDIR /opt/app

RUN ./gradlew clean build --info --stacktrace --no-daemon

FROM eclipse-temurin:11-jre-alpine
RUN mkdir -p /opt/app
# COPY ./build/libs/shadow-*-all.jar /opt/app/app.jar
COPY --from=build-env /opt/app/build/libs/shadow-*-all.jar /opt/app/app.jar
CMD java $JAVA_OPTS -jar /opt/app/app.jar
