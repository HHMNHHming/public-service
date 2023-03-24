From openjdk:8-jre-buster
WORKDIR /usr/src/myapp
ADD target/*.jar /usr/src/myapp/app.jar
EXPOSE 21001
ENTRYPOINT ["java", "-jar", "./app.jar"]