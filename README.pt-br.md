# Spring boot com Redis e Postgres usando Docker

[![en](https://img.shields.io/badge/lang-en-blue.svg)](https://github.com/andresinho20049/spring-authservice-with-docker/src/master/README.md)

### Projeto iniciado baseado em Spring boilerplate
Este projeto de inicialização do Spring foi desenvolvido com as configurações de autenticação padrão em mente e
documentação para servir de base para o desenvolvimento de outros projetos.


**Sobre:**

 - Regras de acesso com JWT
 - Tecnologias usadas
    - Java 8
    - Spring Boot 2.7.0
    - Spring JPA
    - Spring Security 5.6.4     
    - Spring Secutiry Oauth2 Autoconfigure 2.1.5
    - Springfox (Swagger) 3.0.0
    - Postgres
    - Redis
    - Lombok - [Ajuda para configurar lombok](https://projectlombok.org/setup/eclipse)
    - Log4J
    - Projeto Maven
- Application.properties - Default
    - Port: 5000
    - Profile: dev
    - Base path: /api
    - Encrypt: bcrypt
    - Hibernate DDL: update
- ExceptionHandler
    - ProjectException:
        - Status: 400
        - Description: Exceção provocada, n motivos, mas principalmente regra de negócio
    - AuthorizationException:
        - Status: 403
        - Description: Acesso negado com RuntimeException
        
> Não é necessário ter Java, Maven, Postgres ou redis instalados, todos os serviços serão executados em containers Docker.

## Pré requisitos
 - Docker
    - Instalar Docker [here](https://docs.docker.com/engine/install/)
 - Arquivo .env
    - Exemplo:
    ```properties
    DB_AUTHSERVICE_DATABASE_NAME=authservice_database
    DB_AUTHSERVICE_USERNAME=postgres
    DB_AUTHSERVICE_PASSWORD=pg_postgres
    ```

## Como começar
1. Clone o projeto Git
 ```git
    git clone https://{your-user}@bitbucket.org/codderaurateam/spring-authservice-with-docker.git
 ```
 
2. Dentro da pasta do projeto
```sh
	cd spring-authservice-with-docker
```
 
3. Crie um arquivo .env na pasta raiz do projeto
```sh
	tee -a ./.env <<EOF
    DB_AUTHSERVICE_DATABASE_NAME=authservice_database
    DB_AUTHSERVICE_USERNAME=postgres
    DB_AUTHSERVICE_PASSWORD=pg_postgres
```
> Para sair escreva EOF, depois precione ENTER

4. Execute docker compose com parâmetros do arquivo env
```sh
    docker compose --env-file=./.env up --build
```

### Autenticação
Está configurado por padrão para iniciar no perfil Dev, 
neste perfil é criado um usuário de suporte ao iniciar o spring

_Usuário padrão_:     
Username: admin@email.com   
Password: strongPassword@1234

Com o projeto em execução, visite a página do Swagger para testar os endpoints
  - No navegador digite a url: http://localhost:5000/api/swagger-ui/index.html
  - ou se preferir [click aqui](http://localhost:5000/api/swagger-ui/index.html)

visite também **redis console**, executando a porta **8001**

## Modelos
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

## Visualização
![Visualizacao](https://github.com/andresinho20049/spring-authservice-with-docker/blob/master/media/preview-started.gif)
How to started

## Considerações
Este projeto foi desenvolvido para poder utilizá-lo como serviço de autenticação para outras aplicações, como sites


> **Projeto:** Spring boot com Redis e Postgres usando Docker      
> **Autor:** André Carlos [(andresinho20049)](https://github.com/andresinho20049)    
