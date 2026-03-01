FROM maven:3.9.3-eclipse-temurin-20 as maven
RUN apt-get update && apt-get -y install gnuplot && rm -rf /var/lib/apt/lists/*
COPY pom.xml pom.xml
COPY src src
COPY utf8.sh utf8.sh
COPY *.plot ./
RUN mvn clean install 
CMD ["./utf8.sh"]
#CMD ["java", "-server", "-Xms4g", "-Xmx4g", "-XX:-UseBiasedLocking", "-XX:+UseStringDeduplication", "-XX:+UseNUMA", "-XX:+UseParallelGC", "-jar", "app.jar"]
