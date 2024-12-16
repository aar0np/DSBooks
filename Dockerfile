FROM amazoncorretto:21

# get DSAlbums JAR file
COPY target/dsbooks-0.0.1-SNAPSHOT.jar dsbooks.jar

# open port for http endpoint
EXPOSE 8080

ENTRYPOINT ["java","-jar","dsbooks.jar"]
