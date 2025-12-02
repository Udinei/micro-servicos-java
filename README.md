# 🛒 iCompras - Sistema de Microserviços

Sistema de e-commerce desenvolvido com arquitetura de microserviços utilizando Spring Boot, Java 21 e PostgreSQL.

## 📋 Sobre o Projeto

O **iCompras** é um sistema modular de gerenciamento de compras online, construído seguindo os princípios de microserviços. Cada serviço é independente, escalável e possui sua própria base de dados.

## 🏗️ Arquitetura

O projeto é composto por três componentes principais:
 
### Microserviços

- **clientes** - Gerenciamento de clientes e cadastros
- **produtos** - Catálogo e gestão de produtos
- **icompras-servicos** - Infraestrutura e serviços auxiliares

### Infraestrutura

- **PostgreSQL** - Banco de dados relacional (3 schemas isolados)
- **Docker** - Containerização dos serviços de infraestrutura

## 🚀 Tecnologias

- **Java 21** - Linguagem de programação
- **Spring Boot 3.4.4** - Framework para microserviços
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL 17.4** - Banco de dados
- **Lombok** - Redução de código boilerplate
- **Maven** - Gerenciamento de dependências
- **Docker & Docker Compose** - Containerização

## 📦 Estrutura do Projeto

```
icompras/
├── clientes/                    # Microserviço de clientes
│   ├── src/
│   └── pom.xml
├── produtos/                    # Microserviço de produtos
│   ├── src/
│   │   ├── controller/         # Endpoints REST
│   │   ├── service/            # Lógica de negócio
│   │   ├── repository/         # Acesso a dados
│   │   └── model/              # Entidades JPA
│   ├── pom.xml
│   └── api-tests.http          # Testes de API
├── icompras-servicos/          # Infraestrutura
│   └── database/
│       ├── init.sql            # Script de inicialização
│       └── postgres-docker-compose.yml
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
# Terminal 1 - Serviço de Clientes
cd clientes
mvn spring-boot:run

# Terminal 2 - Serviço de Produtos
cd produtos
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

### Clientes API (porta 8081)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/clientes` | Listar todos os clientes |
| GET | `/api/clientes/{codigo}` | Buscar cliente por código |
| POST | `/api/clientes` | Criar novo cliente |
| PUT | `/api/clientes/{codigo}` | Atualizar cliente |
| DELETE | `/api/clientes/{codigo}` | Deletar cliente |

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

### Listar produtos

```bash
curl http://localhost:8081/api/produtos
```

### PowerShell

```powershell
$body = @{
    nome = "Mouse Logitech"
    valorUnitario = 89.90
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8081/api/produtos" `
    -Method Post `
    -Body $body `
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

Instale a extensão **REST Client** e use o arquivo `produtos/api-tests.http` para testar os endpoints.

### Testes com PowerShell

```powershell
# Listar produtos
Invoke-RestMethod -Uri "http://localhost:8081/api/produtos" -Method Get
```

## 📚 Documentação Adicional

- [Comandos Docker](icompras-servicos/database/DOCKER_COMMANDS.md)
- [Endpoints da API](produtos/API_ENDPOINTS.md)
- [Uso do Lombok](produtos/LOMBOK_USAGE.md)

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'feat: adicionar MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## ✨ Autor

Desenvolvido por [Seu Nome](https://github.com/seu-usuario)

---

⭐ Se este projeto foi útil para você, considere dar uma estrela!
