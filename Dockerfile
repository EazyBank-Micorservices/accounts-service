#start with a base image containg a java runtime
FROM openjdk:17-jdk-slim

#information around who maintains the image
MAINTAINER "dennisgithnji.tech"

# add the application jar to the image
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar


#exceute the application
ENTRYPOINT ["java","-jar","accounts-0.0.1-SNAPSHOT.jar"]


