FROM openjdk:19-jdk-alpine
# TODO This can be used to individually identify and push to ECR or in Pipeline.
ARG GITHASH

LABEL maintainer="gopal.tayal@kreuzwerker.de"
EXPOSE 8080

COPY build/libs/aws-starter-*.jar /opt/aws-starter/aws-starter.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /opt/aws-starter/aws-starter.jar"]