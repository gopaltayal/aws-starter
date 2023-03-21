# Documentation for AWS Starter Project
## Introduction
The project is a culmination of various development related practices and terminology.
It acts as a reference point for certain pieces of code that you would require in your day-to-day development activities like:
1. How to practice Hexagonal Architecture?
2. How to write unit tests, integration tests and end-to-end tests?
3. How to set up a docker-compose file for dependencies like the database?
4. How to set up a docker file for deployment of the application?
5. How to write GIT commits?

Besides this, the main aim of the project is to introduce best practices for dealing with the most common AWS resources that you would interact with.
For example :
1. How to set up a code pipeline?
2. How to maintain the infrastructure via CDK?
3. How to use local stack for testing?
4. How to connect to various environments like staging and prod?
5. How to utilise common AWS services like VPC, RDS, S3?
6. How to deploy to ECS?

In parallel, the project will also be implemented in a Serverless re-design. This will be done so that both the server-dependent version and the serverless version lie in the same code base, in order to compare and highlight the differences between both.
These architectures will further be documented using best practices for documentation like ADRs and C4 model.

My suggestion would be to follow the project commit after commit to understand what was added at what step and complement it with the corresponding documentation.
Please feel free to add comments on which aspect of it is unclear or what could be done to improve!

## ADRs

## C4 Model

## AWS Diagram

## Local Setup
1. Use the docker-compose.yml file to start the database.
```
docker-compose up -d
```
2. Run the application! Use the profile 'local' (You can add the following VM options to run in debug mode: "-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005)
3. Test the application on [Local Swagger](http://localhost:8080/swagger-ui/index.html#/)
4. The database credentials for local development can be found in the [application.yml](/src/main/resources/application.yml) file.
5. Stop the application! and clean up the containers
```
docker-compose down
```

## Local Setup in Docker Container
1. Start the Database using docker-compose, check the network created, as the docker container would need to be attached to it to access the database.
2. Build JAR and Package in build/libs folder
```
./gradlew build -x test  
```
3. Build Local Image on Linux/Mac and start containers
```
docker buildx build --platform=linux/amd64 -t aws-starter-app .
```
4. Run the container, notice the network is attached and the port is bound before the image name!
```
docker run --network=aws-starter_default -e "SPRING_PROFILES_ACTIVE=local-container" -dp 8080:8080 aws-starter-app  
```
5. Check the application on [Local Swagger](http://localhost:8080/swagger-ui/index.html#/)
6. Stop the container when done with the container ID
```
docker stop container_id
```
