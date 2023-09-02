# Spring boot with Redis and Postgres using Docker

[![pt-br](https://img.shields.io/badge/lang-pt--br-green.svg)](https://github.com/andresinho20049/spring-authservice-with-docker/blob/master/README.pt-br.md)

### Project started from Spring boilerplate
This Spring boot project was developed with the default authentication settings in mind and 
documentation to serve as a basis for developing other projects.


**About:**

 - Role-based access control with JWT
 - technology used
    - Java 8
    - Spring Boot 2.7.0
    - Spring JPA
    - Spring Security 5.6.4     
    - Spring Secutiry Oauth2 Autoconfigure 2.1.5
    - Springfox (Swagger) 3.0.0
    - Postgres:13
    - Redis
    - Lombok - [Help setting up lombok](https://projectlombok.org/setup/eclipse)
    - Log4J
    - Project Maven
- Application.properties - Default
    - Port: 5000
    - Profile: dev
    - Base path: /api
    - Encrypt: bcrypt
    - Hibernate DDL: update
- ExceptionHandler
    - ProjectException:
        - Status: 400
        - Description: Exception provoked, n reasons, but mainly business rule
    - AuthorizationException:
        - Status: 403
        - Description: Access Denied with RuntimeException
        
> It is not necessary to have Java, Maven, Postgres or redis installed, all services will run in Docker containers.

## Pre requirements
 - Docker
    - Install docker [here](https://docs.docker.com/engine/install/)
 - File .env
    - Example:
    ```properties
    DB_AUTHSERVICE_DATABASE_NAME=authservice_database
    DB_AUTHSERVICE_USERNAME=postgres
    DB_AUTHSERVICE_PASSWORD=pg_postgres
    ```

## Getting Started
1. Git clone project
 ```git
    git clone https://{your-user}@bitbucket.org/codderaurateam/spring-authservice-with-docker.git
 ```
 
2. Go into the project folder
```sh
	cd spring-authservice-with-docker
```
 
3. Create a file .env in root folder project
```sh
	tee -a ./.env <<EOF
    DB_AUTHSERVICE_DATABASE_NAME=authservice_database
    DB_AUTHSERVICE_USERNAME=postgres
    DB_AUTHSERVICE_PASSWORD=pg_postgres
```
> To exit type EOF, then press ENTER

4. Run docker compose with env file parameters
```sh
    docker compose --env-file=./.env up --build
```

### Authentication
It is configured by default to start in the Dev profile, 
in this profile a support user is created when starting spring

_The default is_:     
Username: admin@email.com   
Password: strongPassword@1234

With the project running, visit the Swagger page to test the endpoints
  - In the browser type the url: http://localhost:5000/api/swagger-ui/index.html
  - or If you prefer [click here](http://localhost:5000/api/swagger-ui/index.html)

also visit **redis console**, runing port **8001**

## Model
### User
```json
{
  "id": 1,
  "name": "Admin",
  "email": "admin@email.com",
  "updatePassword": true,
  "active": true,
  "roles": [
    {
      "name": "ROLE_ADMIN"
    }
  ]
}
```

### Role
```json
[
  {
    "name": "ROLE_ADMIN"
  },
  {
    "name": "ROLE_VIEW_USER"
  },
  {
    "name": "ROLE_CREATE_USER"
  },
  {
    "name": "ROLE_UPDATE_USER"
  },
  {
    "name": "ROLE_DISABLE_USER"
  }
]
```

### JWT Payload Example
```json
{
  "aud": [
    "restservice"
  ],
  "updatePassword": true,
  "user_name": "admin@email.com",
  "scope": [
    "all"
  ],
  "name": "Admin",
  "exp": 1655404387,
  "authorities": [
    "ROLE_ADMIN"
  ],
  "jti": "7d12042b-856b-41a7-b1d9-6acb220840a7",
  "client_id": "52da334b25d96304a09901705846663fef41ce8f"
}
```

## Preview
![Preview](https://github.com/andresinho20049/spring-authservice-with-docker/blob/master/media/preview-started.gif)
How to started

## Considerations
This project was developed to be able to use it as an authentication service for other applications, such as websites


> **Projeto:** Spring boot with Redis and Postgres using Docker      
> **Autor:** André Carlos [(andresinho20049)](https://github.com/andresinho20049)       
