# 📊 Modelo de Dados

## 1. Diagrama Entidade-Relacionamento (ER)

```
┌─────────────────────┐
│     CLIENTES        │
├─────────────────────┤
│ id (PK)             │
│ nome                │
│ email (UNIQUE)      │
│ cpf (UNIQUE)        │
│ telefone            │
│ endereco            │
│ cidade              │
│ estado              │
│ cep                 │
│ ativo               │
│ data_cadastro       │
│ data_atualizacao    │
└──────────┬──────────┘
           │ 1
           │
           │ N
           ▼
┌─────────────────────┐
│     PEDIDOS         │
├─────────────────────┤
│ id (PK)             │
│ codigo (UNIQUE)     │
│ cliente_id (FK)     │◄─── CLIENTES
│ data_pedido         │
│ status              │
│ total               │
│ observacoes         │
│ tipo_pagamento      │
│ chave_pagamento     │
│ codigo_rastreio     │
│ url_nf              │
│ data_entrega_est    │
│ data_criacao        │
│ data_atualizacao    │
└──────────┬──────────┘
           │ 1
           │
           │ N
           ▼
┌─────────────────────────┐
│    ITENS_PEDIDO         │
├─────────────────────────┤
│ id (PK)                 │
│ pedido_id (FK)          │◄─── PEDIDOS
│ produto_id (FK)         │
│ quantidade              │
│ preco_unitario          │
│ subtotal                │
└──────────┬──────────────┘
           │
           │ N
           │
           │ 1
           ▼
┌─────────────────────┐
│    PRODUTOS         │
├─────────────────────┤
│ id (PK)             │
│ codigo (UNIQUE)     │
│ nome                │
│ descricao           │
│ preco               │
│ categoria           │
│ sku (UNIQUE)        │
│ estoque             │
│ estoque_minimo      │
│ imagem_url          │
│ ativo               │
│ data_criacao        │
│ data_atualizacao    │
└─────────────────────┘
```

## 2. Cardinalidade

| Relacionamento | Tipo | Descrição |
|---|---|---|
| CLIENTES → PEDIDOS | 1:N | Um cliente pode ter vários pedidos |
| PEDIDOS → ITENS_PEDIDO | 1:N | Um pedido pode ter vários itens |
| PRODUTOS → ITENS_PEDIDO | 1:N | Um produto pode aparecer em vários pedidos |
| CLIENTES ← PEDIDOS | N:1 | Vários pedidos pertencem a um cliente |

## 3. Entidades Principais

### CLIENTES
```
PK: id
Relacionamentos:
  - 1:N com PEDIDOS (um cliente múltiplos pedidos)
Índices:
  - email (busca e validação de duplicata)
  - cpf (busca e validação de duplicata)
  - ativo (filtro de clientes ativos)
```

### PRODUTOS
```
PK: id
Relacionamentos:
  - 1:N com ITENS_PEDIDO
Índices:
  - codigo (busca)
  - sku (busca e validação)
  - categoria (filtro)
  - ativo (visibilidade no catálogo)
  - estoque (alertas de falta)
```

### PEDIDOS
```
PK: id
FK: cliente_id → CLIENTES
Relacionamentos:
  - N:1 com CLIENTES (vários pedidos para um cliente)
  - 1:N com ITENS_PEDIDO (um pedido múltiplos itens)
Índices:
  - codigo (busca por código)
  - cliente_id (pedidos por cliente)
  - status (filtro por status)
  - data_pedido (ordenação e relatórios)
```

### ITENS_PEDIDO
```
PK: id
FK1: pedido_id → PEDIDOS
FK2: produto_id → PRODUTOS
Relacionamentos:
  - N:1 com PEDIDOS
  - N:1 com PRODUTOS
Índices:
  - pedido_id (busca itens de um pedido)
  - produto_id (relatórios de venda por produto)
```

## 4. Atributos e Tipos

### CLIENTES
| Coluna | Tipo | Constraints | Descrição |
|---|---|---|---|
| id | BIGSERIAL | PK, AUTO | Identificador único |
| nome | VARCHAR(255) | NOT NULL | Nome do cliente |
| email | VARCHAR(255) | NOT NULL, UNIQUE | Email |
| cpf | VARCHAR(14) | NOT NULL, UNIQUE | CPF formatado |
| telefone | VARCHAR(20) | - | Telefone |
| endereco | VARCHAR(255) | - | Rua e número |
| cidade | VARCHAR(100) | - | Cidade |
| estado | VARCHAR(2) | - | UF |
| cep | VARCHAR(10) | - | CEP formatado |
| ativo | BOOLEAN | DEFAULT true | Status ativo |
| data_cadastro | TIMESTAMP | DEFAULT NOW | Criação |
| data_atualizacao | TIMESTAMP | DEFAULT NOW | Atualização |

### PRODUTOS
| Coluna | Tipo | Constraints | Descrição |
|---|---|---|---|
| id | BIGSERIAL | PK, AUTO | Identificador |
| codigo | VARCHAR(50) | NOT NULL, UNIQUE | Código produto |
| nome | VARCHAR(255) | NOT NULL | Nome |
| descricao | TEXT | - | Descrição detalhada |
| preco | DECIMAL(10,2) | NOT NULL | Preço em reais |
| categoria | VARCHAR(100) | NOT NULL | Categoria |
| sku | VARCHAR(100) | NOT NULL, UNIQUE | SKU |
| estoque | INTEGER | NOT NULL, DEFAULT 0 | Quantidade em estoque |
| estoque_minimo | INTEGER | NOT NULL, DEFAULT 10 | Mínimo |
| imagem_url | VARCHAR(500) | - | URL da imagem |
| ativo | BOOLEAN | DEFAULT false | Ativo no catálogo |
| data_criacao | TIMESTAMP | DEFAULT NOW | Criação |
| data_atualizacao | TIMESTAMP | DEFAULT NOW | Atualização |

### PEDIDOS
| Coluna | Tipo | Constraints | Descrição |
|---|---|---|---|
| id | BIGSERIAL | PK, AUTO | Identificador |
| codigo | VARCHAR(50) | NOT NULL, UNIQUE | Código pedido |
| cliente_id | BIGINT | NOT NULL, FK | Referência cliente |
| data_pedido | TIMESTAMP | DEFAULT NOW | Data do pedido |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'PENDENTE' | Status |
| total | DECIMAL(10,2) | NOT NULL | Valor total |
| observacoes | TEXT | - | Instruções |
| tipo_pagamento | VARCHAR(50) | - | Forma pagamento |
| chave_pagamento | VARCHAR(100) | - | Chave transação |
| codigo_rastreio | VARCHAR(100) | - | Rastreio logística |
| url_nf | VARCHAR(500) | - | URL nota fiscal |
| data_entrega_est | TIMESTAMP | - | Entrega estimada |
| data_criacao | TIMESTAMP | DEFAULT NOW | Criação |
| data_atualizacao | TIMESTAMP | DEFAULT NOW | Atualização |

### ITENS_PEDIDO
| Coluna | Tipo | Constraints | Descrição |
|---|---|---|---|
| id | BIGSERIAL | PK, AUTO | Identificador |
| pedido_id | BIGINT | NOT NULL, FK | Referência pedido |
| produto_id | BIGINT | NOT NULL, FK | Referência produto |
| quantidade | INTEGER | NOT NULL | Qtd. comprada |
| preco_unitario | DECIMAL(10,2) | NOT NULL | Preço no pedido |
| subtotal | DECIMAL(10,2) | NOT NULL | Qtd × Preço |

## 5. Normalização

- **1NF**: Atributos atômicos, sem repetição
- **2NF**: Sem dependência parcial de chave primária
- **3NF**: Sem dependência transitiva

Todas as tabelas estão em 3NF.

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
