# 📑 Especificação - Serviço de Pedidos

## 1. Funcionalidades

### 1.1 Gerenciamento de Pedidos
- Criar novo pedido
- Atualizar status do pedido
- Consultar histórico
- Cancelar pedido

### 1.2 Itens do Pedido
- Adicionar itens (produtos)
- Remover itens
- Atualizar quantidade
- Calcular subtotal

### 1.3 Rastreamento
- Gerar código de rastreio
- Consultar status
- Notificar cliente

### 1.4 Integração
- Validar cliente
- Validar produtos disponíveis
- Processar pagamento

## 2. Endpoints da API

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/pedidos` | Criar novo pedido |
| GET | `/api/pedidos` | Listar todos |
| GET | `/api/pedidos/{id}` | Buscar por ID |
| PUT | `/api/pedidos/{id}` | Atualizar pedido |
| PATCH | `/api/pedidos/{id}/status` | Atualizar status |
| DELETE | `/api/pedidos/{id}` | Cancelar pedido |
| GET | `/api/pedidos/cliente/{clienteId}` | Pedidos por cliente |
| GET | `/api/pedidos/status/{status}` | Pedidos por status |

## 3. Modelo de Dados

```json
{
  "id": 1,
  "codigo": "PED-20240115-001",
  "cliente_id": 10,
  "cliente_nome": "João Silva",
  "data_pedido": "2024-01-15T10:30:00Z",
  "status": "CONFIRMADO",
  "itens": [
    {
      "id": 1,
      "produto_id": 5,
      "produto_nome": "Notebook Dell",
      "quantidade": 1,
      "preco_unitario": 3500.00,
      "subtotal": 3500.00
    }
  ],
  "total": 3500.00,
  "observacoes": "Entregar em horário comercial",
  "tipo_pagamento": "CARTAO_CREDITO",
  "chave_pagamento": "PAG-2024-001",
  "codigo_rastreio": "BR123456789",
  "url_nf": "https://example.com/nf/NF-001.pdf",
  "data_entrega_estimada": "2024-01-20T18:00:00Z",
  "data_criacao": "2024-01-15T10:30:00Z"
}
```

## 4. Status do Pedido

| Status | Descrição |
|--------|-----------|
| PENDENTE | Aguardando confirmação de pagamento |
| CONFIRMADO | Pedido confirmado e pago |
| PROCESSANDO | Sendo separado no armazém |
| ENVIADO | Já saiu para entrega |
| ENTREGUE | Entregue ao cliente |
| CANCELADO | Pedido cancelado |
| DEVOLVIDO | Retornado pelo cliente |

## 5. Tipos de Pagamento

- BOLETO
- CARTAO_CREDITO
- PIX
- TRANSFERENCIA_BANCARIA

## 6. Validações

- **Cliente**: Deve estar cadastrado e ativo
- **Itens**: Mínimo 1, máximo conforme limite do sistema
- **Quantidade**: Deve ser maior que 0
- **Produtos**: Devem estar disponíveis
- **Total**: Deve ser maior que zero
- **Status**: Transições válidas

## 7. DTOs

### Request - Novo Pedido
```json
{
  "cliente_id": 10,
  "itens": [
    {
      "produto_id": 5,
      "quantidade": 1
    }
  ],
  "observacoes": "Entregar em horário comercial",
  "tipo_pagamento": "CARTAO_CREDITO"
}
```

### Response - Pedido
```json
{
  "id": 1,
  "codigo": "PED-20240115-001",
  "cliente_id": 10,
  "cliente_nome": "João Silva",
  "data_pedido": "2024-01-15T10:30:00Z",
  "status": "CONFIRMADO",
  "itens": [...],
  "total": 3500.00,
  "tipo_pagamento": "CARTAO_CREDITO",
  "codigo_rastreio": "BR123456789",
  "data_criacao": "2024-01-15T10:30:00Z"
}
```

## 8. Fluxo de um Pedido

```
1. Cliente cria pedido
   ↓
2. Sistema valida cliente e produtos
   ↓
3. Sistema calcula total
   ↓
4. Sistema integra com serviço bancário
   ↓
5. Pagamento processado
   ↓
6. Status: CONFIRMADO
   ↓
7. Gera código de rastreio
   ↓
8. Pedido separado (PROCESSANDO)
   ↓
9. Pedido enviado (ENVIADO)
   ↓
10. Pedido entregue (ENTREGUE)
```

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
