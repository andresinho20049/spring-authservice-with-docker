# Spring boot com Redis e Postgres usando Docker

[![en](https://img.shields.io/badge/lang-en-blue.svg)](https://github.com/andresinho20049/spring-authservice-with-docker/blob/master/README.md)

**Spring boilerplate** <br/>
Este projeto de inicialização do Spring foi desenvolvido pensando nas configurações de autenticação padrão e na documentação para servir de base para o desenvolvimento de outros projetos.


## :speech_balloon: Sobre:
 - Controle de acesso baseado em regras com JWT :key:
 - Tecnologias usadas :on:
    - Java 8
    - Maven 3.6.3
    - Spring Boot 2.7.0
    	- Starter Web
     	- Starter Data JPA
      	- Starter Test (JUnit / Mockito)
      	- Starter Log4J2
      	- Devtools
    - Spring Security 5.6.4     
    	- Secutiry Oauth2 Autoconfigure 2.1.5
     	- Security Test 
    - Springfox (Swagger) 3.0.0
    - Lombok - [Help setting up lombok](https://projectlombok.org/setup/eclipse)
    - Database
    	- Postgres:13 (Relational Database)
    	- Starter Data Redis (Token Store)
     	- h2database (Profile Test)
- Application.properties - Default :page_with_curl:
    - Port: 5000
    - Profile: dev
    - Base path: /api
    - Encrypt: bcrypt
    - Hibernate DDL: update
- ExceptionHandler :x:
    - ProjectException:
        - Status: 400
        - Description: Exception provoked, n reasons, but mainly business rule
    - AuthorizationException:
        - Status: 403
        - Description: Access Denied with RuntimeException
        
> Não é necessario ter Java, Maven, Postgres ou redis instalado, todos os serviços vão executar em containers Docker.

## :arrow_double_down: Pré requisitos
 - Docker :arrow_down_small:
    - Instalar docker [here](https://docs.docker.com/engine/install/)
 - Arquivo .env :clipboard:
    - Exemplo:
    ```properties
    DB_AUTHSERVICE_DATABASE_NAME=authservice_database
    DB_AUTHSERVICE_USERNAME=postgres
    DB_AUTHSERVICE_PASSWORD=pg_postgres

    CLIENT_ID=52da334b25d96304a09901705846663fef41ce8f
    CLIENT_SECRET=99214c1e0dd20c56e76d4b2716b39e63a38e8d9b
    ```

## :up: Como começar
1. Git clone do projeto
 ```git
 git clone https://github.com/andresinho20049/spring-authservice-with-docker.git
 ```
 
2. Entre na pasta do projeto
```sh
cd spring-authservice-with-docker
```
 
3. Crie um arquivo `.env` na pasta raiz do projeto
```sh
tee -a ./.env <<EOF
DB_AUTHSERVICE_DATABASE_NAME=authservice_database
DB_AUTHSERVICE_USERNAME=postgres
DB_AUTHSERVICE_PASSWORD=pg_postgres

CLIENT_ID=52da334b25d96304a09901705846663fef41ce8f
CLIENT_SECRET=99214c1e0dd20c56e76d4b2716b39e63a38e8d9b
```
> Para sair digite EOF, então pressione ENTER

4. Execute docker compose com env file parametros
```sh
docker compose --env-file=./.env up --build
```

### :unlock: Autenticação
Ele é configurado por padrão para iniciar no perfil Dev,
neste perfil um usuário de suporte é criado ao iniciar o Spring

_O user default é_: :sunglasses: <br/>	
Username: admin@email.com   
Password: strongPassword@1234

Com o projeto em execução, visite a página do Swagger para testar os endpoints
  - No navegador digite o path: `/api/swagger-ui/index.html`
  - ou se preferir [click aqui](http://localhost:5000/api/swagger-ui/index.html)

tambem visite **redis console**, rodando na porta **8001**

## :hash: Modelo
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

### JWT Payload Exemplo
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

## :movie_camera: Preview
![Preview](https://github.com/andresinho20049/spring-authservice-with-docker/blob/master/media/preview-started.gif)
Como iniciar

## :copyright: Considerações
Este projeto foi desenvolvido para poder utilizá-lo como serviço de autenticação para outras aplicações, como sites

> **Projeto:** Spring boot com Redis e Postgres usando Docker      
> **Author:** André Carlos [(andresinho20049)](https://github.com/andresinho20049)       
