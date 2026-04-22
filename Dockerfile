FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew && ./gradlew build

CMD java -jar build/libs/*.jar