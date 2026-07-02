FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN sh ./gradlew build -x test

CMD ["java", "-jar", "build/libs/Rentora-0.0.1-SNAPSHOT.jar"]
