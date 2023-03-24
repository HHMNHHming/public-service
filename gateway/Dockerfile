From openjdk:8u312-jre-slim-buster
WORKDIR /usr/src/myapp
ADD target/*.jar /usr/src/myapp/app.jar
EXPOSE 20002
ENTRYPOINT ["java", "-jar", "./app.jar"]