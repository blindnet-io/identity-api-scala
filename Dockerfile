FROM eclipse-temurin:17

COPY target/scala-3.2.0/identity.jar /srv/identity.jar

# Should always be set when deployed anyway, but this is a sane default
ENV BN_ENV=${BN_ENV:-production}
CMD java -jar /srv/identity.jar
