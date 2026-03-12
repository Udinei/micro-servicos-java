# 🗄️ Banco de Dados

## 1. Banco de Dados

- **Tipo**: PostgreSQL
- **Versão**: 12+
- **Encoding**: UTF-8
- **Locale**: pt_BR

## 2. Schema do Banco

### Tabelas Principais

#### CLIENTES
```sql
CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    endereco VARCHAR(255),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(10),
    ativo BOOLEAN DEFAULT true,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_email ON clientes(email);
CREATE INDEX idx_cpf ON clientes(cpf);
CREATE INDEX idx_ativo ON clientes(ativo);
```

#### PRODUTOS
```sql
CREATE TABLE produtos (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    sku VARCHAR(100) NOT NULL UNIQUE,
    estoque INTEGER NOT NULL DEFAULT 0,
    estoque_minimo INTEGER NOT NULL DEFAULT 10,
    imagem_url VARCHAR(500),
    ativo BOOLEAN DEFAULT false,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_codigo ON produtos(codigo);
CREATE INDEX idx_sku ON produtos(sku);
CREATE INDEX idx_categoria ON produtos(categoria);
CREATE INDEX idx_ativo ON produtos(ativo);
CREATE INDEX idx_estoque ON produtos(estoque);
```

#### PEDIDOS
```sql
CREATE TABLE pedidos (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    cliente_id BIGINT NOT NULL REFERENCES clientes(id),
    data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    total DECIMAL(10,2) NOT NULL,
    observacoes TEXT,
    tipo_pagamento VARCHAR(50),
    chave_pagamento VARCHAR(100),
    codigo_rastreio VARCHAR(100),
    url_nf VARCHAR(500),
    data_entrega_estimada TIMESTAMP,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE INDEX idx_codigo ON pedidos(codigo);
CREATE INDEX idx_cliente ON pedidos(cliente_id);
CREATE INDEX idx_status ON pedidos(status);
CREATE INDEX idx_data_pedido ON pedidos(data_pedido);
```

#### ITENS_PEDIDO
```sql
CREATE TABLE itens_pedido (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL REFERENCES pedidos(id) ON DELETE CASCADE,
    produto_id BIGINT NOT NULL REFERENCES produtos(id),
    quantidade INTEGER NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

CREATE INDEX idx_pedido ON itens_pedido(pedido_id);
CREATE INDEX idx_produto ON itens_pedido(produto_id);
```

## 3. Constraints

### Clientes
- Email única e não-nula
- CPF única e não-nula
- Nome não-nulo
- Ativo default true

### Produtos
- Código único e não-nulo
- SKU único e não-nulo
- Preço > 0
- Estoque >= 0
- Estoque mínimo > 0
- Ativo default false

### Pedidos
- Código único e não-nulo
- Cliente_id não-nulo
- Status não-nulo
- Total > 0

### Itens Pedido
- Pedido_id não-nulo (com cascata de deleção)
- Produto_id não-nulo
- Quantidade > 0
- Preço unitário > 0
- Subtotal = quantidade × preço_unitário

## 4. Relacionamentos

```
CLIENTES (1) ──────── (N) PEDIDOS
                          │
                          └─────────── (N) ITENS_PEDIDO
                                            │
                                            └─ (1) PRODUTOS

PRODUTOS (1) ──────── (N) ITENS_PEDIDO
```

## 5. Migrações (Flyway)

Localização: `src/main/resources/db/migration/`

```
V1__Initial_Schema.sql      - Schema inicial
V2__Add_Indexes.sql         - Índices
V3__Add_Constraints.sql     - Constraints adicionais
```

## 6. Backup e Restore

### Backup
```bash
pg_dump -U postgres -d icompras -F c -b -v -f icompras.backup
```

### Restore
```bash
pg_restore -U postgres -d icompras -v icompras.backup
```

## 7. Performance

### Índices
- Email de clientes (busca por email)
- CPF de clientes (validação de duplicata)
- Código de produtos (busca)
- SKU de produtos (validação de duplicata)
- Cliente_id em pedidos (filtro por cliente)
- Status em pedidos (filtro por status)
- Data em pedidos (relatórios)

### Query Optimization
- Usar índices nas colunas frequentemente consultadas
- Evitar N+1 queries com fetch join
- Usar paginação em listagens grandes

## 8. Transactions

### Isolamento
- READ_COMMITTED (padrão PostgreSQL)
- Pedidos: transação completa desde validação até salvamento

### Rollback Automático
- Spring @Transactional gerencia automaticamente
- Rollback em caso de exceção

## 9. Monitoramento

### Queries Lentas
```sql
SELECT query, mean_time, calls
FROM pg_stat_statements
ORDER BY mean_time DESC
LIMIT 10;
```

### Tamanho de Tabelas
```sql
SELECT schemaname, tablename, pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename))
FROM pg_tables
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

## 10. Conexão

```properties
# application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/icompras
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
