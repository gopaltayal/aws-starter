# How to run the application locally?
## Local Setup
1. Use the [docker-compose.yml](../docker-compose.yml) file to start the database with the command `docker-compose up -d`.
2. Run the application via IntelliJ! Use the profile *local* from [application.yml](../src/main/resources/application.yml) (You can add the following VM options to run in debug mode: `-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005`).
3. Test the application on [Local Swagger](http://localhost:8080/swagger-ui/index.html#/).
4. The database credentials for local development can be found in the [application.yml](../src/main/resources/application.yml) file.
5. Stop the application! and clean up the containers : `docker-compose down`.

## Local Setup in Docker Container
1. Start the Database using [docker-compose.yml](../docker-compose.yml) , check the network created, as the docker container would need to be attached to it to access the database.
2. Build JAR and Package in [build/libs](/build/libs) folder using the command in the base folder of the project : `./gradlew build -x test`.
3. Build Local Image on **Linux/Mac** and start containers : `docker buildx build --platform=linux/amd64 -t aws-starter-app .`.
4. Run the container : `docker run --network=aws-starter_default -e "SPRING_PROFILES_ACTIVE=local-container" -dp 8080:8080 aws-starter-app`.
    1. Notice the network is attached using the name from the default network creation from [docker-compose.yml](../docker-compose.yml).
    2. The port is bound before the image name, otherwise it won't be available via your local machine on which the container is hosted.
    3. The profile *local-container* in [application.yml](../src/main/resources/application.yml) is specific to the docker run to allow it to connect to the Database via the container name specified when starting the DB container.
5. Check the application on [Local Swagger](http://localhost:8080/swagger-ui/index.html#/).
6. Stop the container when done with the container ID `docker stop container_id`.
7. Stop the database using `docker-compose down`.
8. Clear any orphan containers using `docker container prune`.