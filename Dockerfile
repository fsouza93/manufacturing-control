# Use JRE 21 (compatível com Spring Boot 3)
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Copia o JAR final (ajuste o nome do arquivo se quiser)
COPY target/*.jar app.jar

# Memória enxuta p/ free tier
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Render exporta a var PORT. O Spring respeita via --server.port=$PORT
EXPOSE 8080
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=${PORT:-8080}"]
