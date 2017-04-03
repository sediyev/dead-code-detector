FROM ubuntu:15.04

# Install Java 8
RUN apt-get update
RUN apt-get install software-properties-common -y
RUN add-apt-repository ppa:webupd8team/java -y
RUN apt-get update
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN apt-get install oracle-java8-installer -y --force-yes
RUN apt-get install oracle-java8-set-default

#SciTools Understand
RUN wget http://builds.scitools.com/all_builds/b844/Understand/Understand-4.0.844-Linux-64bit.tgz && tar -xvzf Understand-4.0.844-Linux-64bit.tgz
RUN echo "Server: scitools-license.devfactory.com 00000000 9000" > scitools/conf/license/locallicense.dat

RUN apt-get install libxrender1 -y
RUN apt-get install libxi6 libgconf-2-4 -y
RUN apt-get install libxtst6 -y

ENV LD_LIBRARY_PATH /usr/lib/jvm/java-8-oracle/jre/lib/amd64:/scitools/bin/linux64
ENV STIHOME /scitools/bin/linux64

#Install aws cli
RUN apt-get install awscli -y

# Configure  env
RUN mkdir app
COPY build/libs/dead-code-detector-1.0.jar /app/api.jar

EXPOSE 80 8080
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /app/api.jar" ]
