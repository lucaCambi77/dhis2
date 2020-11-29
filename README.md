# Dhis2 code challenge

See task [here](Coding%20Test%20-%20Back-End%20-%20Java%20-%20Integration%20Service.pdf)

## Requirements : 

* Git
* Java11
* Gradle
* Docker
* Docker-Compose

## Getting Started

* Clone locally 
```bash
git clone git@github.com:lucaCambi77/dhis2.git
```

* Build the project 
```bash
./gradlew clean build
```

## Run application

* Build Docker image
```bash
docker build -t dhis2 .
```

* Start Application
```bash
docker-compose up -d
```

## Stop application

```bash
docker-compose down
```

## Authentication

In memory authentication is in place for spring security with Basic Authentication

There is only one user:

user : *dhis2*

password : *dhis2*

## Tests

Embedded Redis cache is in use for tests

Execution of tests is in build/reports/

In jacoco folder we can also find code coverage in html format

## Documentation : 

http://localhost:5000/swagger-ui.html#/

## Gradle Tasks

```

./gradlew test              // runs only tests
./gradlew build             // build project
./gradlew build -x test     // build project and skip tests
./gradlew clean build       // unvalidate cache and build

```

## Sonarqube

We need to have sonarqube installed [see](https://docs.sonarqube.org/latest/)

After executing test we can create a sonarqube report with
```
./gradlew build jacocoTestReport sonarqube
```

In build/sonar we can check results for our task
