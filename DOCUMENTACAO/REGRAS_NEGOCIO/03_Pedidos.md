# 💼 Regras de Negócio - Pedidos

## 1. Criação de Pedidos

### RN201 - Requisitos Obrigatórios
- Cliente deve estar ativo
- Mínimo 1 item no pedido
- Todos os produtos devem estar disponíveis
- Total deve ser maior que zero

### RN202 - Código do Pedido
- Gerado automaticamente
- Formato: PED-YYYYMMDD-NNNN
- Único no sistema
- Imutável após criação

### RN203 - Validação de Cliente
- Cliente deve estar cadastrado
- Cliente deve estar ativo
- Cliente não pode ter pedidos com status suspeito

## 2. Itens do Pedido

### RN204 - Mínimo e Máximo de Itens
- Mínimo: 1 item por pedido
- Máximo: conforme configuração (ex: 100 itens)
- Cada item deve ter quantidade > 0

### RN205 - Validação de Produtos
- Produto deve estar ativo
- Produto deve ter estoque suficiente
- Preço deve estar disponível

### RN206 - Quantidade
- Deve ser número inteiro positivo
- Não pode ser maior que estoque disponível
- Quantidade mínima: 1

### RN207 - Preço Unitário
- Capturado no momento do pedido
- Congelado para esse pedido
- Se preço mudar, não afeta pedidos anteriores

## 3. Cálculo de Total

### RN208 - Cálculo do Subtotal
- Subtotal = Σ(quantidade × preço_unitário)
- Por cada item do pedido

### RN209 - Cálculo do Total
- Total = Subtotal + frete - desconto
- Não pode ser negativo
- Mínimo: R$ 0,01

## 4. Status do Pedido

### RN210 - Transições de Status
```
PENDENTE → CONFIRMADO → PROCESSANDO → ENVIADO → ENTREGUE
    ↓          ↓              ↓           ↓          ↓
  CANCELADO   CANCELADO    CANCELADO   CANCELADO  DEVOLVIDO
```

### RN211 - Status Inicial
- Todo novo pedido começa em PENDENTE
- Aguarda confirmação de pagamento

### RN212 - PENDENTE → CONFIRMADO
- Ocorre após pagamento bem-sucedido
- Chave de pagamento deve ser gerada
- Notificar cliente

### RN213 - CONFIRMADO → PROCESSANDO
- Manual (equipe de fulfillment)
- Estoque já foi reservado

### RN214 - PROCESSANDO → ENVIADO
- Manual (equipe de fulfillment)
- Código de rastreio gerado
- Notificar cliente com rastreio

### RN215 - ENVIADO → ENTREGUE
- Atualizado por sistema de logística
- Ou confirmado pelo cliente

### RN216 - Cancelamento
- Pode ser cancelado de PENDENTE ou CONFIRMADO
- Não pode ser cancelado após ENVIADO
- Estoque é restaurado
- Reembolso processado

## 5. Pagamento

### RN217 - Integração Bancária
- Pedido não pode sair de PENDENTE sem pagamento confirmado
- Sistema deve integrar com serviço bancário
- Chave de pagamento gerada pelo serviço bancário

### RN218 - Tipos de Pagamento Aceitos
- BOLETO
- CARTAO_CREDITO
- PIX
- TRANSFERENCIA_BANCARIA

### RN219 - Validação de Pagamento
- Validar dados de pagamento
- Integrar com banco para autorização
- Gerar chave única de transação

## 6. Rastreamento

### RN220 - Código de Rastreio
- Gerado quando pedido entra em PROCESSANDO
- Informado ao cliente
- Integrado com transportadora

### RN221 - Consulta de Rastreio
- Cliente pode consultar status a qualquer momento
- Deve exibir histórico de transições

## 7. Estimativa de Entrega

### RN222 - Data de Entrega Estimada
- Calculada ao confirmar pedido
- Baseada em: tipo de frete, localidade, estoque
- Comunicada ao cliente

### RN223 - Atualização de Prazo
- Se atrasar, notificar cliente
- Revisar contrato de frete

## 8. Exclusão

### RN224 - Cancelamento vs Deleção
- Pedidos nunca são deletados
- Apenas cancelados ou arquivados
- Histórico preservado para auditoria

## 9. Controle de Estoque

### RN225 - Reserva de Estoque
- Ao criar pedido: estoque é RESERVADO
- Ao confirmar pagamento: estoque fica COMPROMETIDO
- Ao enviar: estoque é DEDUZIDO

### RN226 - Liberação de Estoque
- Se pedido for cancelado: estoque é liberado
- Se pagamento falhar: estoque é liberado

## 10. Observações

### RN227 - Campo de Observações
- Opcional
- Espaço para instruções especiais de entrega
- Máximo 500 caracteres

## 11. Notificações

### RN228 - Notificações de Pedido
- Confirmação de pedido recebido (ao criar)
- Confirmação de pagamento (ao pagar)
- Pedido em processamento
- Saída para entrega (com rastreio)
- Pedido entregue
- Cancelamento (se aplicável)

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
