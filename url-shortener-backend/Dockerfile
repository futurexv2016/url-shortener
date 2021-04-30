FROM adoptopenjdk/openjdk11:alpine

ARG arg_port_expose=8080

EXPOSE $arg_port_expose
ADD ./target/*.jar /opt/shorten-url/app.jar
WORKDIR /opt/shorten-url
ENTRYPOINT java $JAVA_OPTS -jar app.jar

