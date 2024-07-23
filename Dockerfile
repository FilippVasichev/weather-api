FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app

COPY build/libs/weather-api-1.1.jar /app/weather-api.jar

EXPOSE 8080

CMD ["sh", "-c", "java ${ARGS} -jar weather-api.jar"]