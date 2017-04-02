FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD build/libs/dead-code-detector-1.0.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]






FROM ubuntu:14.04

# Install Java 8
RUN apt-get update
RUN apt-get install software-properties-common -y
RUN add-apt-repository ppa:webupd8team/java -y
RUN apt-get update
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN apt-get install oracle-java8-installer -y --force-yes
RUN apt-get install oracle-java8-set-default

#Install aws cli
RUN apt-get install awscli -y

# Configure  env
RUN mkdir app
COPY api.jar /app/api.jar

EXPOSE 80 8080
ENTRYPOINT [ "sh", "-c", "java -jar /app/api.jar" ]
