FROM maven:3.8.4-openjdk-11 as build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk11:ubi
ARG ARG_BOT_NAME
ARG ARG_BOT_TOKEN
ARG ARG_BOT_DB_USERNAME
ARG ARG_BOT_DB_PASSWORD
ENV BOT_NAME=$ARG_BOT_NAME
ENV BOT_TOKEN=$ARG_BOT_TOKEN
ENV BOT_DB_USERNAME=$ARG_BOT_DB_USERNAME
ENV BOT_DB_PASSWORD=$ARG_BOT_DB_PASSWORD
COPY --from=build /usr/app/target/*.jar /app/app.jar
ENTRYPOINT ["java","-Dspring.datasource.password=${BOT_DB_PASSWORD}", "-Dbot.username=${BOT_NAME}", "-Dbot.token=${BOT_TOKEN}", "-Dspring.datasource.username=${BOT_DB_USERNAME}", "-jar", "/app/app.jar"]