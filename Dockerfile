# Etapa 1 - Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia pom.xml e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código e compila
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2 - Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia o jar compilado da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta do Render
CMD ["java", "-jar", "app.jar", "--server.port=${PORT}"]
