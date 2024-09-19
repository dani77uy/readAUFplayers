# Usar una imagen base de OpenJDK
FROM eclipse-temurin:21-jre

# Establecer el directorio de trabajo
WORKDIR /app

RUN mkdir /app/init-data

# Copiar el archivo JAR de la aplicación al contenedor
COPY target/read-auf-players.jar app.jar
COPY target/classes/init-data/. /app/init-data/.

# Exponer el puerto en el que la aplicación se ejecutará
EXPOSE 9774 27017

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
