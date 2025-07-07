# Usa un'immagine con Java e Maven preinstallati
FROM maven:3.9.5-eclipse-temurin-17

# Copia tutto il progetto
COPY . .

# Entra nella cartella del backend
WORKDIR /app/backend

# Compila l'app
RUN mvn clean package -DskipTests

# Esegui l'app
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
