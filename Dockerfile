
FROM openjdk:latest

ENV APP_HOME=d:/DEV/foreman-ST-PC
WORKDIR $APP_HOME
ADD target/foreman-st.jar foreman-st.jar

ENTRYPOINT ["java", "-jar","foreman-st.jar"]
EXPOSE 8080

