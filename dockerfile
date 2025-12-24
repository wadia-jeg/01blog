FROM eclipse-temurin:21.0.8_9-jdk-jammy As build
WORKDIR /opt/01blog/
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install

FROM eclipse-temurin:21.0.8_9-jre-jammy as final
WORKDIR /opt/01blog/
EXPOSE  8088
COPY --from=build /opt/01blog/target/*.jar /opt/01blog/*.jar
ENTRYPOINT [ "java", "-jar", "/opt/01blog/*.jar" ] 






