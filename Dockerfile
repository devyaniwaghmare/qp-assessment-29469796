FROM openjdk:17
ADD target/grocerybooking-docker.jar grocerybooking-docker.jar
ENTRYPOINT ["java","-jar","/grocerybooking-docker.jar"]