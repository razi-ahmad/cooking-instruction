# Abnamro Assessment

## Setup instruction

#### Prerequisite

- Git 2.x
- Container 
  - Docker 
  - Docker-Compose
- Alternative
  - Maven 3.x
  - Java 11

Clone repository

```bash
git clone https://github.com/razi-ahmad/cooking-instruction.git
```

### Run application with docker

  ```bash
  docker-compose -p cooking-instruction up -d
  ```
-----------------------------------------
## Run application without docker 
Build project

  ```bash
  mvn clean package
  ```
Run Project
  ```bash
  java -jar target/cooking-instruction-0.0.1-SNAPSHOT.jar
  ```
-----------------------------------------

Open the swagger interface in the browser

* http://localhost:9005/recipe/swagger-ui/index.html

-----------------------------------------

Run test cases

  ```bash
  mvn clean test
  ```

-----------------------------------------
## Solution
The database H2 is used for fast response, it can be changed to any other relational database. 
The application is scalable to full fill the requests, in that case need to use external database instead of in memory.


## Area to improve
- Validations need to add to perform checks before saving data
- API Documentation need to update
- More test cases can be added

