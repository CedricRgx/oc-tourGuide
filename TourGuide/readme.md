# TourGuide Project

TourGuide is a Java application built with Maven and deployed using Docker. This guide will help you to build, test, and deploy the Docker image.

## Prerequisites

Before you start, make sure you have installed:

- Java 17
- Spring Boot 3.1.1
- JUnit 5
- [Docker](https://www.docker.com/get-started): To create and manage containers
- [Git](https://git-scm.com/): To clone the project
- A Docker Hub account: To push (upload) Docker images online

## Clone the Repository

The first step is to copy the project. To do this, use these commands:

```bash
git clone https://github.com/your-username/your-repository.git
cd your-repository
```
This will download the project’s source code into a folder on your local machine.

## How to have gpsUtil, rewardCentral and tripPricer dependencies available ?
- mvn install:install-file -Dfile=/libs/gpsUtil.jar -DgroupId=gpsUtil -DartifactId=gpsUtil -Dversion=1.0.0 -Dpackaging=jar
- mvn install:install-file -Dfile=/libs/RewardCentral.jar -DgroupId=rewardCentral -DartifactId=rewardCentral -Dversion=1.0.0 -Dpackaging=jar
- mvn install:install-file -Dfile=/libs/TripPricer.jar -DgroupId=tripPricer -DartifactId=tripPricer -Dversion=1.0.0 -Dpackaging=jar

## Build and Test the Project

To build (compile) and test the project locally using Maven, use the following commands:

```bash
mvn compile
mvn test
```

If the tests pass, you can then create a `.jar` file that contains your application:

```bash
mvn package
```

The `.jar` file will be generated in the `target` directory.

## Build the Docker Image

Now that the project is ready, you can create a Docker image. Make sure Docker is installed and running, then type:

```bash
docker build -t your-dockerhub-username/tourguide:latest .
```

This command will build a Docker image using the `Dockerfile` located in the root of the project.

## Run the Docker Container Locally

To run the application locally with Docker, use this command:

```bash
docker run -d -p 8080:8080 your-dockerhub-username/tourguide:latest
```

This command will start the container and make the application accessible on port `8080`.

## Push the Docker Image to Docker Hub

If you want to upload your Docker image to Docker Hub, first log in to your Docker Hub account:

```bash
docker login
```

Then, push the Docker image online with this command:

```bash
docker push your-dockerhub-username/tourguide:latest
```

## Automated Deployment with GitHub Actions

This project includes a GitHub Actions workflow that automatically builds, tests, and pushes the Docker image to Docker Hub whenever changes are pushed to the `main` branch.

### Setting Up GitHub Actions

1. Go to your GitHub repository and add two new secrets: `DOCKER_USERNAME` (your Docker Hub username) and `DOCKER_PASSWORD` (your Docker Hub password or access token).

### GitHub Actions Workflow

The included YAML file defines two jobs:

- **Build and Test Job:** This job compiles the Java project, runs the tests, and packages the application using Maven.
- **Docker Build and Push Job:** This job builds the Docker image and pushes it to Docker Hub.

The workflow is triggered in every push to the `main` branch.

### Accessing the Application

Once the Docker container is running, you can access the application by navigating to:

```
http://localhost:8080
```

in your web browser.

## Conclusion

You have successfully built, tested, and deployed the Docker image for the TourGuide project. This process can be integrated into your CI/CD pipeline using the provided GitHub Actions workflow.