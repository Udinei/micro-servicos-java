# Design Document

## Overview

Este design descreve a arquitetura e estrutura de três projetos Java para o sistema iCompras:

1. **clientes**: Microserviço para gerenciamento de clientes
2. **produtos**: Microserviço para gerenciamento de produtos  
3. **icompras-servicos**: Projeto de infraestrutura contendo configurações Docker para serviços auxiliares

Todos os projetos utilizarão Java 21, Spring Boot 3.4.4, Maven, e seguirão as melhores práticas de desenvolvimento de microserviços.

## Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────┐
│                  iCompras System                        │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────┐              ┌──────────────┐       │
│  │   clientes   │              │   produtos   │       │
│  │ microservice │              │ microservice │       │
│  └──────┬───────┘              └──────┬───────┘       │
│         │                              │               │
│         └──────────────┬───────────────┘               │
│                        │                               │
│                        ▼                               │
│         ┌──────────────────────────────┐              │
│         │   icompras-servicos          │              │
│         │   (Infrastructure)           │              │
│         │                              │              │
│         │  - PostgreSQL (Docker)       │              │
│         │  - Message Broker (Docker)   │              │
│         │  - Storage/Bucket (Docker)   │              │
│         └──────────────────────────────┘              │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### Project Structure

Cada microserviço (clientes e produtos) seguirá a estrutura padrão do Spring Boot:

```
<microservice-name>/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/github/udinei/icompras/<service>/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       └── Application.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application.yml
│   └── test/
│       └── java/
├── pom.xml
└── README.md
```

O projeto icompras-servicos terá estrutura focada em infraestrutura:

```
icompras-servicos/
├── docker/
│   ├── postgres/
│   │   └── init.sql
│   ├── broker/
│   └── storage/
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Components and Interfaces

### Cliente Service Components

**Controller Layer**
- Responsável por receber requisições HTTP REST
- Validação básica de entrada
- Retorno de respostas HTTP apropriadas

**Service Layer**
- Lógica de negócio do domínio de clientes
- Orquestração de operações
- Tratamento de regras de negócio

**Repository Layer**
- Interface com banco de dados usando Spring Data JPA
- Operações CRUD básicas
- Queries customizadas quando necessário

**Model Layer**
- Entidades JPA representando clientes
- DTOs para transferência de dados
- Validações de domínio

### Produto Service Components

**Controller Layer**
- Responsável por receber requisições HTTP REST
- Validação básica de entrada
- Retorno de respostas HTTP apropriadas

**Service Layer**
- Lógica de negócio do domínio de produtos
- Orquestração de operações
- Tratamento de regras de negócio

**Repository Layer**
- Interface com banco de dados usando Spring Data JPA
- Operações CRUD básicas
- Queries customizadas quando necessário

**Model Layer**
- Entidades JPA representando produtos
- DTOs para transferência de dados
- Validações de domínio

### iCompras Servicos Components

**Docker Compose Configuration**
- Orquestração de containers
- Definição de redes
- Configuração de volumes
- Variáveis de ambiente

**PostgreSQL Configuration**
- Scripts de inicialização do banco
- Configuração de usuários e permissões
- Schemas para cada microserviço

**Message Broker Configuration**
- Configuração de filas e tópicos
- Políticas de retry e dead letter
- Configurações de segurança

**Storage Configuration**
- Configuração de buckets
- Políticas de acesso
- Configuração de retenção

## Data Models

### Cliente Service

```java
@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

### Produto Service

```java
@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;
    private String categoria;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

### Database Schema

Cada microserviço terá seu próprio schema no PostgreSQL:
- `clientes_db` para o Cliente Service
- `produtos_db` para o Produto Service

## Configuration Files

### application.properties (Cliente Service)

```properties
spring.application.name=clientes
server.port=8081

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/clientes_db
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### application.properties (Produto Service)

```properties
spring.application.name=produtos
server.port=8082

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/produtos_db
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### docker-compose.yml (iCompras Servicos)

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: icompras-postgres
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./docker/postgres/init.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - icompras-network

volumes:
  postgres_data:

networks:
  icompras-network:
    driver: bridge
```

### init.sql (PostgreSQL Initialization)

```sql
-- Create databases for each microservice
CREATE DATABASE clientes_db;
CREATE DATABASE produtos_db;

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE clientes_db TO postgres;
GRANT ALL PRIVILEGES ON DATABASE produtos_db TO postgres;
```

## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system-essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

### Acceptance Criteria Testing Prework

1.1 THE Cliente Service SHALL be initialized with Java version 21
  Thoughts: This is a configuration requirement that can be verified by checking the pom.xml file contains the correct Java version specification
  Testable: yes - example

1.2 THE Cliente Service SHALL use Spring Boot version 3.4.4
  Thoughts: This is a configuration requirement that can be verified by checking the pom.xml file contains the correct Spring Boot version
  Testable: yes - example

1.3 THE Cliente Service SHALL use Maven as build tool with JAR packaging
  Thoughts: This can be verified by checking that pom.xml exists and specifies JAR packaging
  Testable: yes - example

1.4-1.6 THE Cliente Service SHALL have correct artifact, name, and group ID
  Thoughts: These are configuration values that can be verified in pom.xml
  Testable: yes - example

1.7-1.10 THE Cliente Service SHALL include required dependencies
  Thoughts: This can be verified by checking that all required dependencies are present in pom.xml
  Testable: yes - example

2.1-2.10 (Same as 1.1-1.10 but for Produto Service)
  Thoughts: Same verification approach as Cliente Service
  Testable: yes - example

3.1-3.2 WHEN services start THEN system SHALL load application properties
  Thoughts: This is testing that the Spring context loads successfully with configuration files
  Testable: yes - property

3.3-3.6 Services SHALL have database and JPA configuration
  Thoughts: This can be verified by checking that configuration files contain required properties
  Testable: yes - example

4.1-4.4 Services SHALL organize code in standard packages
  Thoughts: This can be verified by checking that the directory structure follows the expected pattern
  Testable: yes - example

5.1-5.10 iCompras Servicos SHALL have correct configuration and structure
  Thoughts: These are configuration and structure requirements that can be verified
  Testable: yes - example

6.1-6.4 iCompras Servicos SHALL include docker-compose configuration
  Thoughts: This can be verified by checking that docker-compose.yml exists and contains required services
  Testable: yes - example

7.1-7.5 WHEN Maven build is executed THEN system SHALL compile and start successfully
  Thoughts: This is testing that the projects can be built and run without errors
  Testable: yes - property

### Property Reflection

After reviewing all testable criteria, most are configuration verification examples rather than universal properties. The main properties that apply across multiple inputs are:

- Property 1 (from 3.1-3.2): Application context loading
- Property 2 (from 7.1-7.5): Build and startup success

The configuration checks (1.1-1.10, 2.1-2.10, 3.3-3.6, 4.1-4.4, 5.1-5.10, 6.1-6.4) are all examples that verify specific configuration values exist and are correct. These don't need to be properties since they're checking concrete values, not behaviors across a range of inputs.

### Correctness Properties

Property 1: Application context initialization
*For any* microservice (clientes or produtos), when the Spring Boot application starts with valid configuration, the application context should initialize successfully without errors
**Validates: Requirements 3.1, 3.2**

Property 2: Maven build success
*For any* project (clientes, produtos, or icompras-servicos), when Maven clean install is executed, the build should complete successfully producing a JAR artifact
**Validates: Requirements 7.1, 7.2, 7.3**

Property 3: Database connectivity
*For any* microservice (clientes or produtos), when the application starts with PostgreSQL running, the datasource connection should be established successfully
**Validates: Requirements 3.3, 3.4**

## Error Handling

### Build Errors
- Maven dependency resolution failures should provide clear error messages
- Java version mismatches should be detected early in build process
- Missing configuration files should fail fast with descriptive errors

### Runtime Errors
- Database connection failures should be logged with connection details
- Spring context initialization failures should provide stack traces
- Configuration property errors should indicate which property is missing or invalid

### Docker Errors
- Container startup failures should be logged
- Network connectivity issues should be reported
- Volume mounting errors should provide clear feedback

## Testing Strategy

### Unit Testing

We will use JUnit 5 for unit testing with the following approach:

**Configuration Tests**
- Verify pom.xml contains correct Java version (21)
- Verify pom.xml contains correct Spring Boot version (3.4.4)
- Verify pom.xml contains all required dependencies
- Verify pom.xml has correct artifact, name, and group ID
- Verify application.properties contains required database configuration
- Verify application.properties contains required JPA configuration

**Structure Tests**
- Verify expected package structure exists (controller, service, repository, model)
- Verify main application class exists with @SpringBootApplication annotation
- Verify docker-compose.yml exists in icompras-servicos
- Verify init.sql exists in icompras-servicos

**Example Tests**
- Test that Cliente Service pom.xml has artifact "clientes"
- Test that Produto Service pom.xml has artifact "produtos"
- Test that iCompras Servicos pom.xml has artifact "icompras-servicos"
- Test that docker-compose.yml defines PostgreSQL service

### Property-Based Testing

We will use **JUnit-Quickcheck** as the property-based testing library for Java.

Configuration: Each property-based test will run a minimum of 100 iterations.

**Property Tests**

Property Test 1: Application Context Initialization
- **Feature: microservices-icompras, Property 1: Application context initialization**
- Generate different valid application.properties configurations
- For each configuration, verify Spring context loads successfully
- Validates: Requirements 3.1, 3.2

Property Test 2: Maven Build Success  
- **Feature: microservices-icompras, Property 2: Maven build success**
- For each project (clientes, produtos, icompras-servicos)
- Execute Maven clean install
- Verify build completes with exit code 0 and JAR is produced
- Validates: Requirements 7.1, 7.2, 7.3

Property Test 3: Database Connectivity
- **Feature: microservices-icompras, Property 3: Database connectivity**
- Generate different valid database configurations
- For each configuration, verify datasource connection succeeds
- Validates: Requirements 3.3, 3.4

### Integration Testing

- Test that microservices can connect to PostgreSQL running in Docker
- Test that docker-compose successfully starts all services
- Test that microservices can start after infrastructure is running

### Testing Approach

1. **Configuration First**: Verify all configuration files are correct before testing runtime behavior
2. **Build Verification**: Ensure all projects compile successfully
3. **Infrastructure Setup**: Verify Docker services start correctly
4. **Service Startup**: Test that microservices can start and connect to infrastructure
5. **Property Validation**: Run property-based tests to verify universal behaviors

The dual testing approach ensures:
- Unit tests catch specific configuration errors and structural issues
- Property tests verify that the system behaves correctly across different valid configurations
- Integration tests validate that all components work together
