# Usar una imagen oficial de Maven con JDK 17
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo pom.xml y descargar las dependencias
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copiar todo el código del proyecto al contenedor
COPY src ./src

# Compilar el proyecto usando Maven
RUN mvn clean package -DskipTests

# Usar una imagen más ligera para correr la aplicación
FROM eclipse-temurin:17-jdk-jammy

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo .jar generado en la fase anterior al contenedor
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto de la aplicación (ajusta al puerto que usa tu aplicación Spring Boot)
EXPOSE 8080

# Definir el comando para correr la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
