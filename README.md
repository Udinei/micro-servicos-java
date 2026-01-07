# 🛒 iCompras - Sistema de Microserviços

Sistema de e-commerce desenvolvido com arquitetura de microserviços utilizando Spring Boot, Java 21 e PostgreSQL.

## 📋 Sobre o Projeto

O **iCompras** é um sistema modular de gerenciamento de compras online, construído seguindo os princípios de microserviços. Cada serviço é independente, escalável e possui sua própria base de dados.

## 🏗️ Arquitetura

O projeto é composto por quatro componentes principais:
 
### Microserviços

- **clientes** - Gerenciamento de clientes e cadastros
- **produtos** - Catálogo e gestão de produtos
- **pedidos** - Gestão de pedidos e itens de pedido
- **icompras-servicos** - Infraestrutura e serviços auxiliares

### Infraestrutura

- **PostgreSQL** - Banco de dados relacional (3 schemas isolados)
- **Docker** - Containerização dos serviços de infraestrutura

## 🚀 Tecnologias

- **Java 21** - Linguagem de programação
- **Spring Boot 3.4.4** - Framework para microserviços
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL 17.4** - Banco de dados
- **MapStruct** - Mapeamento entre DTOs e entidades
- **Bean Validation** - Validação de dados
- **Lombok** - Redução de código boilerplate
- **Maven** - Gerenciamento de dependências
- **Docker & Docker Compose** - Containerização

## 📦 Estrutura do Projeto

```
icompras/
├── clientes/                    # Microserviço de clientes
│   ├── src/
│   │   ├── main/java/
│   │   │   ├── controller/     # Endpoints REST
│   │   │   ├── service/        # Lógica de negócio
│   │   │   ├── repository/     # Acesso a dados
│   │   │   └── model/          # Entidades JPA
│   │   └── test/               # Testes unitários
│   ├── pom.xml
│   └── api-tests-clientes.http # Testes de API
├── produtos/                    # Microserviço de produtos
│   ├── src/
│   │   ├── main/java/
│   │   │   ├── controller/     # Endpoints REST
│   │   │   ├── service/        # Lógica de negócio
│   │   │   ├── repository/     # Acesso a dados
│   │   │   └── model/          # Entidades JPA
│   │   └── test/               # Testes unitários
│   ├── pom.xml
│   ├── api-tests.http          # Testes de API
│   ├── API_ENDPOINTS.md        # Documentação da API
│   └── LOMBOK_USAGE.md         # Guia do Lombok
├── pedidos/                     # Microserviço de pedidos
│   ├── src/
│   │   ├── main/java/
│   │   │   ├── controller/     # Endpoints REST
│   │   │   ├── service/        # Lógica de negócio
│   │   │   ├── repository/     # Acesso a dados
│   │   │   ├── model/          # Entidades JPA
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   └── mapper/         # MapStruct mappers
│   │   └── test/               # Testes unitários e property-based
│   ├── pom.xml
│   └── api-tests-pedidos.http  # Testes de API
├── icompras-servicos/          # Infraestrutura
│   ├── src/
│   └── database/
│       ├── init.sql            # Script de inicialização
│       ├── postgres-docker-compose.yml
│       └── DOCKER_COMMANDS.md  # Comandos Docker
├── .kiro/                      # Configurações Kiro
│   └── specs/                  # Especificações de features
│       └── pedidos-crud-fix/   # Spec do CRUD de pedidos
└── README.md
```

## 🔧 Pré-requisitos

- Java 21 ou superior
- Maven 3.8+
- Docker e Docker Compose
- Git

## ⚙️ Configuração e Instalação

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/icompras.git
cd icompras
```

### 2. Inicie o banco de dados

```bash
docker compose -f icompras-servicos/database/postgres-docker-compose.yml up -d
```

### 3. Compile os projetos

```bash
# Compilar todos os projetos
mvn clean install -DskipTests

# Ou compilar individualmente
cd clientes && mvn clean install
cd ../produtos && mvn clean install
cd ../icompras-servicos && mvn clean install
```

### 4. Execute os microserviços

```bash
# Terminal 1 - Serviço de Clientes (porta 8082)
cd clientes
mvn spring-boot:run

# Terminal 2 - Serviço de Produtos (porta 8081)
cd produtos
mvn spring-boot:run

# Terminal 3 - Serviço de Pedidos (porta 8083)
cd pedidos
mvn spring-boot:run
```

## 🌐 Endpoints

### Produtos API (porta 8081)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/produtos` | Listar todos os produtos |
| GET | `/api/produtos/{codigo}` | Buscar produto por código |
| GET | `/api/produtos/buscar?nome={nome}` | Buscar produtos por nome |
| POST | `/api/produtos` | Criar novo produto |
| PUT | `/api/produtos/{codigo}` | Atualizar produto |
| DELETE | `/api/produtos/{codigo}` | Deletar produto |

### Clientes API (porta 8082)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/clientes` | Listar todos os clientes |
| GET | `/api/clientes/{codigo}` | Buscar cliente por código |
| POST | `/api/clientes` | Criar novo cliente |
| PUT | `/api/clientes/{codigo}` | Atualizar cliente |
| DELETE | `/api/clientes/{codigo}` | Deletar cliente |

### Pedidos API (porta 8083)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/pedidos` | Listar todos os pedidos |
| GET | `/api/pedidos/{codigo}` | Buscar pedido por código |
| GET | `/api/pedidos/cliente/{codigoCliente}` | Buscar pedidos por cliente |
| GET | `/api/pedidos/status/{status}` | Buscar pedidos por status |
| POST | `/api/pedidos` | Criar novo pedido |
| PUT | `/api/pedidos/{codigo}` | Atualizar pedido |
| PATCH | `/api/pedidos/{codigo}/status` | Atualizar status do pedido |
| DELETE | `/api/pedidos/{codigo}` | Deletar pedido |

## 📝 Exemplos de Uso

### Criar um produto

```bash
curl -X POST http://localhost:8081/api/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Notebook Dell",
    "valorUnitario": 3500.00
  }'
```

### Criar um pedido

```bash
curl -X POST http://localhost:8083/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "codigoCliente": 1,
    "observacoes": "Entrega urgente",
    "status": "PENDENTE",
    "total": 3589.90,
    "dadosPagamento": {
      "tipoPagamento": "CARTAO_CREDITO",
      "numeroCartao": "1234567890123456",
      "codigoAutorizacao": "AUTH123"
    },
    "itens": [
      {
        "codigoProduto": 1,
        "quantidade": 1,
        "valorUnitario": 3500.00
      },
      {
        "codigoProduto": 2,
        "quantidade": 1,
        "valorUnitario": 89.90
      }
    ]
  }'
```

### Listar produtos

```bash
curl http://localhost:8081/api/produtos
```

### PowerShell

```powershell
# Criar produto
$body = @{
    nome = "Mouse Logitech"
    valorUnitario = 89.90
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8081/api/produtos" `
    -Method Post `
    -Body $body `
    -ContentType "application/json"

# Criar pedido
$pedido = @{
    codigoCliente = 1
    observacoes = "Pedido via PowerShell"
    status = "PENDENTE"
    total = 89.90
    dadosPagamento = @{
        tipoPagamento = "PIX"
        chavePix = "usuario@email.com"
    }
    itens = @(
        @{
            codigoProduto = 2
            quantidade = 1
            valorUnitario = 89.90
        }
    )
} | ConvertTo-Json -Depth 3

Invoke-RestMethod -Uri "http://localhost:8083/api/pedidos" `
    -Method Post `
    -Body $pedido `
    -ContentType "application/json"
```

## 🗄️ Banco de Dados

O sistema utiliza PostgreSQL com 3 schemas isolados:

- **icomprasclientes** - Dados de clientes
- **icomprasprodutos** - Catálogo de produtos
- **icompraspedidos** - Pedidos e itens

### Conexão

- **Host:** localhost
- **Porta:** 5555
- **Usuário:** postgres
- **Senha:** postgres

## 🐳 Docker

### Comandos úteis

```bash
# Iniciar serviços
docker compose -f icompras-servicos/database/postgres-docker-compose.yml up -d

# Parar serviços
docker compose -f icompras-servicos/database/postgres-docker-compose.yml down

# Ver logs
docker logs db_i_compras

# Acessar PostgreSQL
docker exec -it db_i_compras psql -U postgres
```

## 🧪 Testes

### Testes com REST Client (VS Code)

Instale a extensão **REST Client** e use os arquivos de teste:
- `produtos/api-tests.http` - Testes da API de produtos
- `clientes/api-tests-clientes.http` - Testes da API de clientes  
- `pedidos/api-tests-pedidos.http` - Testes da API de pedidos

### Testes com PowerShell

```powershell
# Listar produtos
Invoke-RestMethod -Uri "http://localhost:8081/api/produtos" -Method Get
```

## 🏛️ Arquitetura de Camadas

### Pedidos (Exemplo de implementação completa)

```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
Database (PostgreSQL)

DTOs ←→ MapStruct Mappers ←→ Entities
```

### Funcionalidades Implementadas

- **DTOs com Bean Validation** - Validação automática de entrada
- **MapStruct** - Mapeamento automático entre DTOs e entidades
- **Relacionamentos JPA** - Pedido ↔ ItemPedido (OneToMany/ManyToOne)
- **Transações** - Operações atômicas com `@Transactional`
- **Testes Unitários** - Cobertura completa do service layer
- **Property-Based Testing** - Testes com dados gerados automaticamente
- **Dados de Pagamento** - Suporte a múltiplos tipos de pagamento

## 📚 Documentação Adicional

- [Comandos Docker](icompras-servicos/database/DOCKER_COMMANDS.md)
- [Endpoints da API](produtos/API_ENDPOINTS.md)
- [Uso do Lombok](produtos/LOMBOK_USAGE.md)
- [Especificações Kiro](.kiro/specs/) - Documentação de features

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'feat: adicionar MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## ✨ Autor

Desenvolvido por [Udinei Silva](https://github.com/udinei)
Projeto do curso [Spring Boot + Kafka: Arquitetura Completa de Microservices](https://www.udemy.com/course/spring-boot-kafka) ministrado por [Dougllas Sousa](https://www.udemy.com/user/dougllas-sousa/)

---

⭐ Se este projeto foi útil para você, considere dar uma estrela!
