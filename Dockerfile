FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=target/*.jar
ENV BOT_NAME="test_mangalib_bot"
ENV BOT_TOKEN="5384303669:AAHgmQVdQPwP4IjKw5yCCpyvilGmLf7edWE"
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "-Dbot.username=${BOT_NAME}", "-Dbot.token=${BOT_TOKEN}", "-jar", "/app.jar"]