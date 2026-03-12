# 🏗️ Arquitetura - Visão Geral

## 1. Arquitetura de Microsserviços

O sistema iCompras foi desenhado como uma arquitetura de **microsserviços**, onde cada serviço é responsável por um domínio específico de negócio.

```
┌─────────────────────────────────────────────────────────────────┐
│                          GATEWAY (Futuro)                        │
│                  (Entrada única para all requests)               │
└────────┬──────────────────────────┬──────────────────────────┬───┘
         │                          │                          │
    ┌────▼────────┐       ┌────────▼──────┐        ┌──────────▼────┐
    │   CLIENTES   │       │   PRODUTOS    │        │    PEDIDOS     │
    │   SERVICE    │       │   SERVICE     │        │    SERVICE     │
    └────┬────────┘       └────────┬──────┘        └──────────┬────┘
         │                         │                          │
         │ PostgreSQL              │ PostgreSQL               │ PostgreSQL
         │                         │                          │
    ┌────▼──────────────────────────▼───────────────────────▼──────┐
    │                                                               │
    │              SERVIÇO BANCÁRIO (Cliente HTTP)                 │
    │                   (Externa - Integração)                     │
    │                                                               │
    └───────────────────────────────────────────────────────────────┘
```

## 2. Stack Tecnológico

### Backend
- **Linguagem**: Java 21
- **Framework**: Spring Boot 3.5.0
- **Persistência**: Spring Data JPA + Hibernate
- **Database**: PostgreSQL 42.7.5
- **Build**: Maven 3.8+

### Utilitários
- **Lombok**: 1.18.38 (Redução de boilerplate)
- **MapStruct**: 1.6.3 (Mapeamento de DTOs)
- **Validation**: Jakarta Validation 3.0.2

### Protocolo de Comunicação
- **HTTP/REST**: APIs RESTful
- **JSON**: Formato de dados

## 3. Camadas da Aplicação

Cada microsserviço segue a arquitetura em camadas:

```
┌──────────────────────────────────────┐
│         CAMADA DE APRESENTAÇÃO       │
│  (Controllers, Request/Response)     │
├──────────────────────────────────────┤
│         CAMADA DE APLICAÇÃO          │
│  (DTOs, Mappers, Conversão)          │
├──────────────────────────────────────┤
│         CAMADA DE NEGÓCIO            │
│  (Services, Validadores, Regras)     │
├──────────────────────────────────────┤
│         CAMADA DE PERSISTÊNCIA       │
│  (Repositories, Entities, JPA)       │
├──────────────────────────────────────┤
│         CAMADA DE DADOS              │
│  (PostgreSQL, Queries, Transações)   │
└──────────────────────────────────────┘
```

### Detalhamento

**Camada de Apresentação (REST)**
- Controllers que recebem requisições HTTP
- Mapeamento de rotas
- Validação de entrada
- Serialização de resposta

**Camada de Aplicação (DTOs)**
- Conversão de modelos para DTOs
- Mapeadores (Mappers)
- Orquestração de chamadas

**Camada de Negócio (Services)**
- Lógica de negócio
- Validadores
- Orquestração de repositórios
- Transações

**Camada de Persistência (Repository)**
- Interfaces de acesso a dados
- Queries customizadas
- Relacionamentos de entidades

**Camada de Dados (Database)**
- PostgreSQL
- Transações
- Constraints
- Índices

## 4. Padrões Arquiteturais

### MVC - Model View Controller
- Model: Entidades JPA
- View: DTOs de resposta
- Controller: Endpoints REST

### Repository Pattern
- Abstração de acesso a dados
- Reutilização de queries
- Facilita testes

### Service Pattern
- Encapsulamento de lógica de negócio
- Transações
- Validações

### DTO (Data Transfer Object)
- Separação entre modelo interno e API externa
- Segurança (não expor IDs internos)
- Performance (enviar apenas dados necessários)

### Dependency Injection
- Spring gerencia dependências
- Reduz acoplamento
- Facilita testes

## 5. Fluxo de Requisição

```
HTTP Request
    ↓
Controller (mapeia rota e parâmetros)
    ↓
Validação de entrada (@Valid)
    ↓
Mapper (DTO → Entity)
    ↓
Service (aplica regras de negócio)
    ↓
Validador (valida regras específicas)
    ↓
Repository (persiste dados)
    ↓
Transaction (commit/rollback automático)
    ↓
Mapper (Entity → DTO)
    ↓
HTTP Response (JSON)
```

## 6. Segurança de Transações

### @Transactional
- Spring gerencia transações automaticamente
- readOnly = true para operações de leitura
- Rollback automático em exceção

### Níveis de Isolamento
- DEFAULT (recomendado)
- READ_UNCOMMITTED
- READ_COMMITTED
- REPEATABLE_READ
- SERIALIZABLE

## 7. Integração com Serviço Bancário

```
Pedido (PENDENTE)
    ↓
Chama ServicoBancarioClient
    ↓
Envia dados de pagamento
    ↓
Banco processa e retorna chave
    ↓
Pedido → CONFIRMADO
    ↓
Guarda chave de pagamento
```

## 8. Tratamento de Erros

```
Exception Ocorre
    ↓
Spring captura
    ↓
ExceptionHandler processa
    ↓
ErrorResponse criada
    ↓
HTTP status + mensagem de erro
    ↓
Log registrado
```

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
