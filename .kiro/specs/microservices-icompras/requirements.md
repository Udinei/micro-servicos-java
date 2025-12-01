# Requirements Document

## Introduction

Este documento descreve os requisitos para a criação de três projetos Java para o sistema iCompras: dois microserviços (clientes e produtos) e um projeto de infraestrutura (icompras-servicos) que conterá arquivos de configuração para serviços auxiliares como broker de mensageria, banco de dados, e outros recursos que serão executados em containers Docker.

## Glossary

- **Microservice**: Um serviço independente que executa uma função de negócio específica
- **Spring Boot**: Framework Java para criação de aplicações standalone
- **Maven**: Ferramenta de gerenciamento de dependências e build para projetos Java
- **PostgreSQL**: Sistema de gerenciamento de banco de dados relacional
- **JPA**: Java Persistence API para mapeamento objeto-relacional
- **Lombok**: Biblioteca Java que reduz código boilerplate através de anotações
- **REST API**: Interface de programação de aplicações que utiliza protocolo HTTP
- **Cliente Service**: Microserviço responsável pelo gerenciamento de clientes
- **Produto Service**: Microserviço responsável pelo gerenciamento de produtos
- **iCompras Servicos**: Projeto de infraestrutura que contém arquivos de configuração para serviços auxiliares
- **Docker**: Plataforma de containerização para executar aplicações em ambientes isolados
- **Message Broker**: Sistema de mensageria para comunicação assíncrona entre serviços
- **Bucket**: Armazenamento de objetos para arquivos e dados

## Requirements

### Requirement 1

**User Story:** Como desenvolvedor, eu quero criar a estrutura base do microserviço de clientes, para que eu possa desenvolver funcionalidades de gerenciamento de clientes.

#### Acceptance Criteria

1. THE Cliente Service SHALL be initialized with Java version 21
2. THE Cliente Service SHALL use Spring Boot version 3.4.4
3. THE Cliente Service SHALL use Maven as build tool with JAR packaging
4. THE Cliente Service SHALL have artifact name "clientes"
5. THE Cliente Service SHALL have project name "clientes"
6. THE Cliente Service SHALL have group ID "com.github.udinei.icompras"
7. THE Cliente Service SHALL include Spring Web dependency
8. THE Cliente Service SHALL include Spring Data JPA dependency
9. THE Cliente Service SHALL include Lombok dependency
10. THE Cliente Service SHALL include PostgreSQL Driver dependency

### Requirement 2

**User Story:** Como desenvolvedor, eu quero criar a estrutura base do microserviço de produtos, para que eu possa desenvolver funcionalidades de gerenciamento de produtos.

#### Acceptance Criteria

1. THE Produto Service SHALL be initialized with Java version 21
2. THE Produto Service SHALL use Spring Boot version 3.4.4
3. THE Produto Service SHALL use Maven as build tool with JAR packaging
4. THE Produto Service SHALL have artifact name "produtos"
5. THE Produto Service SHALL have project name "produtos"
6. THE Produto Service SHALL have group ID "com.github.udinei.icompras"
7. THE Produto Service SHALL include Spring Web dependency
8. THE Produto Service SHALL include Spring Data JPA dependency
9. THE Produto Service SHALL include Lombok dependency
10. THE Produto Service SHALL include PostgreSQL Driver dependency

### Requirement 3

**User Story:** Como desenvolvedor, eu quero que ambos os microserviços tenham configurações básicas do Spring Boot, para que possam ser executados e conectados ao banco de dados.

#### Acceptance Criteria

1. WHEN the Cliente Service starts THEN the system SHALL load application properties configuration
2. WHEN the Produto Service starts THEN the system SHALL load application properties configuration
3. THE Cliente Service SHALL have database connection configuration for PostgreSQL
4. THE Produto Service SHALL have database connection configuration for PostgreSQL
5. THE Cliente Service SHALL have JPA/Hibernate configuration
6. THE Produto Service SHALL have JPA/Hibernate configuration

### Requirement 4

**User Story:** Como desenvolvedor, eu quero que os projetos tenham uma estrutura de pacotes organizada, para facilitar a manutenção e escalabilidade do código.

#### Acceptance Criteria

1. THE Cliente Service SHALL organize code in packages following standard Spring Boot structure
2. THE Produto Service SHALL organize code in packages following standard Spring Boot structure
3. THE Cliente Service SHALL have separate packages for controllers, services, repositories, and models
4. THE Produto Service SHALL have separate packages for controllers, services, repositories, and models

### Requirement 5

**User Story:** Como desenvolvedor, eu quero criar a estrutura base do projeto de infraestrutura icompras-servicos, para que eu possa gerenciar arquivos de configuração de serviços auxiliares que serão executados em containers Docker.

#### Acceptance Criteria

1. THE iCompras Servicos SHALL be initialized with Java version 21
2. THE iCompras Servicos SHALL use Spring Boot version 3.4.4
3. THE iCompras Servicos SHALL use Maven as build tool with JAR packaging
4. THE iCompras Servicos SHALL have artifact name "icompras-servicos"
5. THE iCompras Servicos SHALL have project name "icompras-servicos"
6. THE iCompras Servicos SHALL have group ID "com.github.udinei.icompras"
7. THE iCompras Servicos SHALL contain directory structure for Docker configuration files
8. THE iCompras Servicos SHALL contain directory structure for message broker configuration
9. THE iCompras Servicos SHALL contain directory structure for database configuration
10. THE iCompras Servicos SHALL contain directory structure for bucket/storage configuration

### Requirement 6

**User Story:** Como desenvolvedor, eu quero que o projeto icompras-servicos tenha arquivos Docker Compose, para orquestrar a execução de todos os serviços auxiliares necessários.

#### Acceptance Criteria

1. THE iCompras Servicos SHALL include a docker-compose.yml file
2. WHEN docker-compose is executed THEN the system SHALL start PostgreSQL database container
3. THE docker-compose configuration SHALL define network configuration for service communication
4. THE docker-compose configuration SHALL define volume mappings for data persistence

### Requirement 7

**User Story:** Como desenvolvedor, eu quero que ambos os microserviços possam ser compilados e executados com sucesso, para validar que a configuração inicial está correta.

#### Acceptance Criteria

1. WHEN Maven build is executed on Cliente Service THEN the system SHALL compile without errors
2. WHEN Maven build is executed on Produto Service THEN the system SHALL compile without errors
3. WHEN Maven build is executed on iCompras Servicos THEN the system SHALL compile without errors
4. WHEN Cliente Service application starts THEN the system SHALL initialize Spring context successfully
5. WHEN Produto Service application starts THEN the system SHALL initialize Spring context successfully
