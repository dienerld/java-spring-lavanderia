FROM openjdk:19-slim AS builder

# Copie o código-fonte da aplicação para o contêiner
COPY . /app

# Defina o diretório de trabalho como /app
WORKDIR /app

# Exponha a porta 8080 para acesso externo
EXPOSE 8080

# Instale as dependências do Maven
RUN apt-get update && apt-get install -y maven

# Construa o projeto
# Execute a aplicação quando o contêiner for iniciado
CMD ["mvn", "clean", "package", "-DskipTests"]


### Production
FROM openjdk:19-alpine

# Copie o código-fonte da aplicação para o contêiner
COPY --from=builder /app/target/*.jar /app.jar

# Defina o diretório de trabalho como /app
WORKDIR /app

# Exponha a porta 8080 para acesso externo
EXPOSE 8080

# Execute a aplicação quando o contêiner for iniciado
CMD ["java", "-jar", "/app.jar"]
