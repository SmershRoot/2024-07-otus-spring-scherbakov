FROM bellsoft/liberica-openjdk-alpine-musl:21.0.1
MAINTAINER scherbakov_a_a

WORKDIR /opt/app

LABEL Description="Серверная часть АИС «БИБЛИОТЕКА»"

COPY /target/*.jar /opt/app/library-api.jar

ENTRYPOINT ["java", "-server", "-jar", "library-api.jar"]

EXPOSE 8080