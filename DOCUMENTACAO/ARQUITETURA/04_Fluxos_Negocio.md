# 🔄 Fluxos de Negócio

## 1. Fluxo de Criação de Pedido

```
Início
  ↓
1. Cliente submete novo pedido com:
   - Cliente ID
   - Lista de itens (produto_id, quantidade)
   - Tipo de pagamento
   - Observações (opcional)
  ↓
2. Validações Iniciais
   ├─ Cliente existe e está ativo?
   ├─ Mínimo 1 item no pedido?
   └─ Todos os campos obrigatórios preenchidos?
  ↓ (Se falhar → Erro 400)
3. Validar Produtos
   ├─ Cada produto existe?
   ├─ Produto está ativo?
   ├─ Quantidade disponível em estoque?
   └─ Preço válido?
  ↓ (Se falhar → Erro 404/422)
4. Calcular Total
   ├─ Para cada item: subtotal = quantidade × preço_unitário
   └─ total = Σ subtotais
  ↓
5. Reservar Estoque (Transiente)
   └─ Estoque será atualizado após pagamento
  ↓
6. Gerar Código do Pedido
   └─ Formato: PED-YYYYMMDD-NNNN
  ↓
7. Salvar Pedido (Status: PENDENTE)
   ├─ Salvar pedido
   ├─ Salvar itens do pedido
   └─ Registrar em auditoria
  ↓
8. Integrar com Serviço Bancário
   ├─ Enviar dados de pagamento
   ├─ Incluir valor total
   └─ Incluir informações do cliente
  ↓
9. Aguardar Resposta do Banco
   ├─ Autorizado? → Próximo passo
   └─ Negado? → Status permanece PENDENTE (cliente tenta novamente)
  ↓
10. Se Autorizado:
    ├─ Chave de pagamento recebida
    ├─ Status do pedido → CONFIRMADO
    ├─ Deduzir estoque dos produtos
    ├─ Gerar código de rastreio
    └─ Salvar chave de pagamento
  ↓
11. Notificar Cliente
    ├─ Email com confirmação
    ├─ Código do pedido
    ├─ Código de rastreio
    └─ Valor total pago
  ↓
12. Retornar Resposta
    └─ PedidoDTO com todos os dados
  ↓
Fim (Sucesso)
```

## 2. Fluxo de Atualização de Status

```
Início
  ↓
1. Usuário solicita mudança de status
   ├─ Pedido ID
   └─ Novo status
  ↓
2. Validações
   ├─ Pedido existe?
   ├─ Transição de status é válida?
   └─ Usuário tem permissão?
  ↓ (Se falhar → Erro)
3. De acordo com novo status:
   
   PENDENTE → CONFIRMADO
   ├─ Validar pagamento
   └─ Confirmar transação
   
   CONFIRMADO → PROCESSANDO
   ├─ Separar itens no armazém
   └─ Registrar separação
   
   PROCESSANDO → ENVIADO
   ├─ Registrar com transportadora
   ├─ Gerar rastreio
   └─ Atualizar código
   
   ENVIADO → ENTREGUE
   ├─ Confirmar entrega
   └─ Finalizar pedido
   
   Qualquer status → CANCELADO
   ├─ Se PENDENTE: apenas liberar
   ├─ Se CONFIRMADO: processar reembolso
   ├─ Se PROCESSANDO+: avisar cliente
   └─ Restaurar estoque
  ↓
4. Atualizar Data de Modificação
  ↓
5. Notificar Cliente (conforme status)
  ↓
6. Registrar em Log de Auditoria
  ↓
Fim (Sucesso)
```

## 3. Fluxo de Pagamento

```
Início
  ↓
1. Pedido em PENDENTE recebe requisição de pagamento
  ↓
2. Validar Dados de Pagamento
   ├─ Tipo válido? (BOLETO, CARTAO, PIX, TRANSFERENCIA)
   ├─ Campos obrigatórios preenchidos?
   └─ Formato correto?
  ↓ (Se falhar → Erro 400)
3. De acordo com tipo:
   
   CARTAO_CREDITO:
   ├─ Validar número (Luhn)
   ├─ Validar data (futura)
   ├─ Validar CVV (3 dígitos)
   └─ Validar nome titular
   
   PIX:
   ├─ Validar chave PIX
   └─ Validar beneficiário
   
   BOLETO:
   ├─ Gerar código de barras
   └─ Enviar ao cliente
   
   TRANSFERENCIA:
   ├─ Validar dados bancários
   └─ Fornecer instruções
  ↓
4. Enviar para Serviço Bancário
   ├─ Cliente HTTP HTTP
   ├─ Dados: tipo, valor, cliente, pedido_id
   └─ Aguardar resposta
  ↓
5. Processar Resposta
   ├─ Autorizado?
   │  ├─ Gerar chave de transação
   │  ├─ Status → CONFIRMADO
   │  ├─ Deduzir estoque
   │  └─ Notificar cliente (SUCESSO)
   │
   ├─ Negado?
   │  ├─ Status → PENDENTE (não muda)
   │  ├─ Registrar motivo da negação
   │  └─ Notificar cliente (FALHA)
   │
   └─ Timeout?
       ├─ Status → PENDENTE
       ├─ Marcar para retry
       └─ Notificar operador
  ↓
Fim
```

## 4. Fluxo de Cancelamento

```
Início
  ↓
1. Solicitação de cancelamento
   ├─ Pedido ID
   └─ Motivo (opcional)
  ↓
2. Validações
   ├─ Pedido existe?
   ├─ Status permite cancelamento? (PENDENTE/CONFIRMADO apenas)
   └─ Pedido já entregue? (Bloquear)
  ↓ (Se falhar → Erro)
3. Se Status = PENDENTE:
   ├─ Apenas liberar reserva de estoque
   └─ Notificar cliente
  ↓
4. Se Status = CONFIRMADO:
   ├─ Iniciar processo de reembolso
   ├─ Integrar com serviço bancário
   ├─ Gerar transação de reembolso
   ├─ Aguardar confirmação
   └─ Notificar cliente
  ↓
5. Restaurar Estoque
   └─ Para cada item do pedido:
       └─ Quantidade += quantidade_pedido
  ↓
6. Atualizar Status → CANCELADO
  ↓
7. Registrar Razão do Cancelamento
  ↓
8. Log de Auditoria
  ↓
Fim
```

## 5. Fluxo de Busca de Pedidos

```
Início
  ↓
1. Cliente solicita busca com filtros:
   ├─ Por ID (opcional)
   ├─ Por código (opcional)
   ├─ Por status (opcional)
   ├─ Por data (opcional)
   └─ Paginação (página, limite)
  ↓
2. Validações
   ├─ Filtros em formato válido?
   ├─ Usuário tem permissão de visualizar?
   └─ (Cliente só vê seus pedidos)
  ↓ (Se falhar → Erro)
3. Construir Query
   ├─ SELECT * FROM pedidos WHERE
   ├─ cliente_id = ? (se cliente)
   ├─ AND status = ? (se filtro status)
   ├─ AND data >= ? (se filtro data)
   └─ ORDER BY data_pedido DESC
  ↓
4. Aplicar Paginação
   ├─ OFFSET = (página - 1) × limite
   └─ LIMIT = limite
  ↓
5. Executar Query
   └─ Banco retorna resultados
  ↓
6. Mapear para DTOs
   ├─ Para cada pedido:
   │  ├─ Mapear dados
   │  ├─ Incluir itens
   │  └─ Mapear itens para DTO
   └─ Retornar lista
  ↓
Fim
```

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
