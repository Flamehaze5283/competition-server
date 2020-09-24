FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD Gateway.jar App.jar
ENTRYPOINT ["java","-jar","/App.jar"]