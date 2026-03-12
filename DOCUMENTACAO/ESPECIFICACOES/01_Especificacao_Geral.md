# 📋 Especificação Geral - Sistema iCompras

## 1. Visão Geral

O **Sistema iCompras** é uma plataforma de e-commerce composta por múltiplos microsserviços, fornecendo funcionalidades de gerenciamento de clientes, produtos, pedidos e pagamentos.

## 2. Objetivos

- ✅ Permitir cadastro e gerenciamento de clientes
- ✅ Gerenciar catálogo de produtos
- ✅ Processar pedidos de forma eficiente
- ✅ Integrar com serviço bancário para pagamentos
- ✅ Manter histórico de transações

## 3. Componentes Principais

### 3.1 Serviço de Clientes (Cliente Service)
- Cadastro de clientes
- Atualização de dados pessoais
- Validação de informações

### 3.2 Serviço de Produtos (Produtos Service)
- Catálogo de produtos
- Gestão de inventário
- Categorização e busca

### 3.3 Serviço de Pedidos (Pedidos Service)
- Criação e gerenciamento de pedidos
- Rastreamento de status
- Cálculo de totais

### 3.4 Serviço Bancário (Servico Bancario)
- Processamento de pagamentos
- Validação de meios de pagamento
- Geração de chaves de transação

## 4. Stack Tecnológico

| Componente | Versão | Descrição |
|-----------|--------|-----------|
| Java | 21 | Linguagem de programação |
| Spring Boot | 3.5.0 | Framework web |
| Spring Data JPA | 3.5.0 | ORM e persistência |
| PostgreSQL | 42.7.5 | Banco de dados |
| Lombok | 1.18.38 | Geração de código |
| MapStruct | 1.6.3 | Mapeamento de DTOs |

## 5. Arquitetura

```
┌─────────────┐
│   Clientes  │
└─────────────┘
       │
       ├─ API REST
       └─ PostgreSQL


┌─────────────┐
│   Produtos  │
└─────────────┘
       │
       ├─ API REST
       └─ PostgreSQL


┌─────────────┐
│   Pedidos   │ ◄─────── Integra com Clientes e Produtos
└─────────────┘
       │
       ├─ API REST
       ├─ PostgreSQL
       └─ Chamadas HTTP para Serviço Bancário


┌──────────────────┐
│ Serviço Bancário │
└──────────────────┘
       │
       └─ Cliente HTTP (integração externa)
```

## 6. Fluxo Geral de um Pedido

```
1. Cliente faz pedido
   ↓
2. Validação de dados (cliente, produtos, quantidade)
   ↓
3. Cálculo do total
   ↓
4. Integração com Serviço Bancário
   ↓
5. Processamento de pagamento
   ↓
6. Confirmação e geração de chave de rastreio
   ↓
7. Pedido finalizado
```

## 7. Padrões Utilizados

- **REST** - APIs RESTful
- **MVC** - Model-View-Controller
- **DTO** - Data Transfer Objects
- **Repository** - Padrão de acesso a dados
- **Service** - Camada de negócio
- **Mapper** - Conversão entre modelos

## 8. Tecnologias de Comunicação

- **HTTP/REST** - Comunicação entre microsserviços
- **JSON** - Formato de dados
- **PostgreSQL** - Persistência

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
