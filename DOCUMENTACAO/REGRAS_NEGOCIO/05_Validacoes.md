# 💼 Regras de Negócio - Validações Gerais

## 1. Validação de Formato

### RN401 - Email
- Padrão: `^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$`
- Obrigatório em alguns contextos
- Único no sistema

### RN402 - CPF
- Padrão: XXX.XXX.XXX-XX (com formatação)
- Validar algoritmo (módulo 11)
- Não aceitar sequências iguais (111.111.111-11)

### RN403 - CNPJ
- Padrão: XX.XXX.XXX/XXXX-XX (com formatação)
- Validar algoritmo (módulo 11)

### RN404 - Telefone
- Padrão: (XX) 9XXXX-XXXX ou (XX) XXXX-XXXX
- Mínimo 10 dígitos, máximo 11
- Apenas números válidos

### RN405 - CEP
- Padrão: XXXXX-XXX
- Validar com serviço de CEP (futuro)

### RN406 - URL
- Deve ser URL válida
- Começar com http:// ou https://
- Acessível e responsiva

## 2. Validação de Tamanho

### RN407 - Strings
- Nome mínimo: 3 caracteres
- Descrição mínima: 10 caracteres
- Máximo: conforme campo específico

### RN408 - Números
- Preço: mínimo R$ 0,01
- Quantidade: mínimo 1, máximo 999.999
- Estoque: mínimo 0, máximo 999.999

## 3. Validação de Relacionamento

### RN409 - Integridade Referencial
- Cliente referenciado deve existir
- Produto referenciado deve existir
- Categoria referenciada deve existir

### RN410 - Cascata de Exclusão
- Não deletar cliente com pedidos
- Não deletar produto com itens em pedidos
- Apenas marcar como inativo

## 4. Validação de Negócio

### RN411 - Duplicação
- Email único por cliente
- CPF/CNPJ único por cliente
- Código do produto único
- SKU do produto único
- Código do pedido único

### RN412 - Valores Válidos
- Preço deve ser maior que zero
- Estoque não pode ser negativo
- Data não pode ser futura (em geral)
- Status deve estar em lista válida

### RN413 - Transições Válidas
- Status só pode transitar para estado válido
- Não permitir transições inválidas
- Registrar tentativa de transição inválida

## 5. Validação Temporal

### RN414 - Data de Validade de Cartão
- Data não pode estar no passado
- Mês e ano válidos
- Formato: MM/YY

### RN415 - Data de Criação vs Atualização
- Data de criação não pode ser alterada
- Data de atualização deve ser posterior a criação
- Ambas no formato ISO 8601

## 6. Validação de Contexto

### RN416 - Validação Condicional
- Cartão obrigatório apenas se tipo_pagamento = CARTAO_CREDITO
- PIX obrigatório apenas se tipo_pagamento = PIX
- Boleto obrigatório apenas se tipo_pagamento = BOLETO

### RN417 - Validação de Estado
- Cliente inativo não pode fazer pedidos
- Produto inativo não pode ser vendido
- Pedido ENTREGUE não pode ser modificado

## 7. Validação de Permissão

### RN418 - Quem Pode Modificar
- Cliente só modifica seus próprios dados
- Admin modifica qualquer coisa
- Funcionário modifica status de pedido (futuro)

### RN419 - Quem Pode Visualizar
- Cliente vê apenas seus próprios pedidos
- Admin vê todos os pedidos
- Funcionário vê conforme permissão (futuro)

## 8. Mensagens de Erro

### RN420 - Padrão de Erro
- Código do erro (ex: ERR_CLIENTE_INVALIDO)
- Mensagem amigável ao usuário
- Campo que causou erro (se aplicável)
- Sugestão de correção

### RN421 - Log de Erros
- Todos os erros devem ser logados
- Incluir stack trace completo
- Não expor informações sensíveis ao cliente

## 9. Validação de API

### RN422 - Request Válido
- Todos os campos obrigatórios preenchidos
- Tipos de dados corretos
- Tamanho de payload válido

### RN423 - Response Consistente
- Sempre JSON
- Incluir HTTP status correto
- Incluir mensagem e dados

## 10. Casos Especiais

### RN424 - Campo Obrigatório vs Condicional
- Campo obrigatório: sempre necessário
- Campo condicional: necessário em certos contextos
- Campo opcional: sempre pode estar vazio

### RN425 - Valor Padrão
- Tipo pagamento padrão: CARTAO_CREDITO (se não especificado)
- Status padrão: ATIVO
- Ativo padrão: true

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
