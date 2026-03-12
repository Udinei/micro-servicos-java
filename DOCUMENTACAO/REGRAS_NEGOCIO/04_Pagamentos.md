# 💼 Regras de Negócio - Pagamentos

## 1. Processamento de Pagamento

### RN301 - Requisitos para Pagamento
- Cliente deve estar ativo
- Pedido deve estar em status PENDENTE
- Dados de pagamento devem estar completos
- Total do pedido deve ser maior que zero

### RN302 - Tipos de Pagamento
- **BOLETO**: Pagamento por código de barras
- **CARTAO_CREDITO**: Cartão de crédito
- **PIX**: Transferência instantânea
- **TRANSFERENCIA_BANCARIA**: TED/DOC

## 2. Validação de Dados de Pagamento

### RN303 - Cartão de Crédito
- Número válido (Luhn algorithm)
- Data de validade futura
- CVV válido (3 dígitos)
- Nome do titular obrigatório

### RN304 - PIX
- Chave PIX válida (CPF, email, telefone ou chave aleatória)
- Identificador do pagador

### RN305 - Boleto
- Dados obrigatórios preenchidos
- Válido por 3 dias úteis (padrão)

### RN306 - Transferência Bancária
- Dados obrigatórios da conta
- Banco, agência, conta

## 3. Integração Bancária

### RN307 - Chamada ao Serviço Bancário
- Enviar dados de pagamento para validação
- Serviço bancário autoriza ou nega
- Gerar chave única de transação

### RN308 - Resposta do Banco
- Autorizado: gerar chave de pagamento
- Negado: devolver erro ao cliente
- Timeout: marcar como pendente (retry)

### RN309 - Chave de Pagamento
- Gerada pelo serviço bancário
- Única e imutável
- Armazenar para auditoria

## 4. Segurança de Pagamento

### RN310 - Dados Sensíveis
- Nunca armazenar número completo do cartão
- Armazenar apenas últimos 4 dígitos + máscara
- CVV nunca armazenar
- Usar HTTPS em toda comunicação

### RN311 - Auditoria
- Registrar todas as tentativas de pagamento
- Registrar autorização/negação
- Preservar logs por 7 anos

## 5. Transações Múltiplas

### RN312 - Pagamento Parcial
- Não permitir pagamentos parciais (versão 1.0)
- Total do pedido deve ser pago de uma vez

### RN313 - Tentativas de Pagamento
- Máximo 3 tentativas por pedido
- Entre tentativas: aguardar 5 minutos
- Após 3 falhas: pedido é cancelado

## 6. Reembolso

### RN314 - Solicitação de Reembolso
- Cliente pode solicitar reembolso em até 30 dias
- Motivo deve ser registrado
- Admin aprova ou nega

### RN315 - Processamento de Reembolso
- Reembolso integral do valor pago
- Retorno em até 5 dias úteis
- Gerar comprovante

### RN316 - Casos de Reembolso Automático
- Pedido cancelado pela loja
- Defeito no produto (constatado)
- Produto não entregue (prazo vencido)

## 7. Reconciliação

### RN317 - Reconciliação Diária
- Comparar pagamentos recebidos com pedidos
- Resolver divergências
- Alertar se houver inconsistência

### RN318 - Relatórios de Pagamento
- Relatório diário de pagamentos recebidos
- Relatório de refusas
- Relatório de pendências

## 8. Fraude

### RN319 - Detecção de Fraude (Futuro)
- Alertar se múltiplas tentativas de clientes diferentes
- Alertar se valor muito acima do normal
- Alertar se CPF não bate com banco

### RN320 - Bloqueio de Fraude
- Bloquear transações suspeitas
- Solicitar validação adicional ao cliente
- Notificar admin

## 9. Integração com Pedidos

### RN321 - Fluxo de Pagamento em Pedido
```
1. Pedido criado (PENDENTE)
2. Cliente inicia pagamento
3. Dados enviados ao serviço bancário
4. Banco autoriza ou nega
5. Se autorizado: Pedido → CONFIRMADO
6. Se negado: Pedido continua PENDENTE, cliente pode tentar novamente
```

### RN322 - Confirmação de Pagamento
- Pedido só sai de PENDENTE após pagamento confirmado
- Gerar confirmação de pagamento
- Enviar recibo ao cliente

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
