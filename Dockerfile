FROM openjdk:19-slim

# Copie o código-fonte da aplicação para o contêiner
COPY . .

# Exponha a porta 8080 para acesso externo
EXPOSE 8080

# Instale as dependências do Maven
RUN apt-get update && apt-get install -y maven && mvn clean package -DskipTests && cp target/*.jar app.jar

# Construa o projeto
# Execute a aplicação quando o contêiner for iniciado
CMD [ "java", "-jar", "app.jar" ]
