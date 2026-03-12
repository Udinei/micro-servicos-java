# 💼 Regras de Negócio - Clientes

## 1. Cadastro de Clientes

### RN001 - Dados Obrigatórios
- Nome do cliente é obrigatório
- Email é obrigatório e deve ser único
- CPF ou CNPJ é obrigatório (depende do tipo de cliente)
- Telefone é obrigatório
- Endereço é obrigatório

### RN002 - Validação de CPF/CNPJ
- CPF deve ser válido (passar na validação do algoritmo)
- CNPJ deve ser válido (passar na validação do algoritmo)
- Não podem estar duplicados no sistema

### RN003 - Email Único
- Cada email deve ser único no sistema
- Email deve estar em formato válido
- Validar antes de salvar

### RN004 - Endereço Obrigatório
- Rua é obrigatória
- Número é obrigatório
- Cidade é obrigatória
- Estado é obrigatório
- CEP é obrigatório e deve estar em formato válido

## 2. Ativação e Inativação

### RN005 - Cliente Ativo por Padrão
- Novo cliente é criado com status ativo por padrão
- Cliente inativo não pode fazer pedidos

### RN006 - Inativação de Cliente
- Cliente só pode ser inativado, não deletado (soft delete)
- Ao inativar, todos os seus pedidos pendentes são cancelados
- Histórico é preservado

## 3. Atualização de Dados

### RN007 - Dados que Podem Ser Alterados
- Nome
- Email (desde que novo email seja único)
- Telefone
- Endereço
- Informações de contato

### RN008 - Dados que Não Podem Ser Alterados
- CPF/CNPJ (após criação)
- Data de cadastro
- ID do cliente

## 4. Exclusão

### RN009 - Exclusão Suave
- Cliente nunca é deletado fisicamente do banco
- Apenas marcado como inativo
- Dados históricos são preservados para auditoria

### RN010 - Reativação
- Cliente inativo pode ser reativado
- Todos os dados são restaurados

## 5. Validações de Segurança

### RN011 - Proteção de Dados
- Email e CPF/CNPJ são sensíveis
- Devem ser validados em tempo real
- Não podem ser expostos em logs completos

### RN012 - Telefone Válido
- Deve estar em formato brasileiro
- Mínimo 10 dígitos, máximo 11
- Formato: (XX) 9XXXX-XXXX ou (XX) XXXX-XXXX

## 6. Limites e Restrições

### RN013 - Quantidade de Pedidos
- Um cliente pode ter múltiplos pedidos simultâneos
- Sem limite definido (configurável)

### RN014 - Crédito
- Cliente não tem limite de crédito no sistema (pagamento à vista)
- Pedido só é confirmado após pagamento

## 7. Notificações

### RN015 - Email de Boas-vindas
- Enviar email após cadastro bem-sucedido
- Conter instruções e dados de acesso

### RN016 - Notificação de Alteração
- Notificar quando email ou telefone forem alterados

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
