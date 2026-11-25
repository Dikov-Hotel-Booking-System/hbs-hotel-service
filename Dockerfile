FROM eclipse-temurin:21-jre-noble

WORKDIR /app

COPY --chown=10001:10001 build/libs/app.jar /app/app.jar

USER 10001:10001

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
