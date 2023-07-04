FROM maven:3.8.4-openjdk-11 as build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk11:ubi
ENV BOT_NAME=test_mangalib_bot
ENV BOT_TOKEN=5384303669:AAHgmQVdQPwP4IjKw5yCCpyvilGmLf7edWE
ENV BOT_DB_USERNAME=SomeUser
ENV BOT_DB_PASSWORD=SomePassword
COPY --from=build /usr/app/target/*.jar /app/app.jar
ENTRYPOINT ["java","-Dspring.datasource.password=${BOT_DB_PASSWORD}", "-Dbot.username=${BOT_NAME}", "-Dbot.token=${BOT_TOKEN}", "-Dspring.datasource.username=${BOT_DB_USERNAME}", "-jar", "/app/app.jar"]