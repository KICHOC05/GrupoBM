# ========= ETAPA 1: CONSTRUCCIÓN =========
FROM exoplatform/ubuntu:24.04 AS build

# Instalar dependencias necesarias
RUN apt-get -qq update && \
    apt-get -qq -y install gnupg ca-certificates curl maven

# Agregar repositorio de Azul Zulu y instalar JDK 25
RUN curl -s https://repos.azul.com/azul-repo.key | gpg --dearmor -o /usr/share/keyrings/azul.gpg
RUN echo "deb [signed-by=/usr/share/keyrings/azul.gpg] https://repos.azul.com/zulu/deb stable main" | tee /etc/apt/sources.list.d/zulu.list
RUN apt-get -qq update && \
    apt-get -qq -y install zulu25-jdk

# Configurar JAVA_HOME (ajusta la ruta según la arquitectura, puede variar)
ENV JAVA_HOME=/usr/lib/jvm/zulu25-ca-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ========= ETAPA 2: EJECUCIÓN =========
FROM exoplatform/ubuntu:24.04

# Instalar solo JRE 25 (más ligero)
RUN apt-get -qq update && \
    apt-get -qq -y install gnupg ca-certificates curl

RUN curl -s https://repos.azul.com/azul-repo.key | gpg --dearmor -o /usr/share/keyrings/azul.gpg
RUN echo "deb [signed-by=/usr/share/keyrings/azul.gpg] https://repos.azul.com/zulu/deb stable main" | tee /etc/apt/sources.list.d/zulu.list
RUN apt-get -qq update && \
    apt-get -qq -y install zulu25-jre

ENV JAVA_HOME=/usr/lib/jvm/zulu25-ca-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

# Utiliza tini como entrypoint (como en el ejemplo)
ENTRYPOINT ["/usr/bin/java", "-jar", "app.jar"]