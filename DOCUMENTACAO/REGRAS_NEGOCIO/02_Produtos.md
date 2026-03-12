# 💼 Regras de Negócio - Produtos

## 1. Criação de Produtos

### RN101 - Dados Obrigatórios
- Código do produto é obrigatório e único
- Nome é obrigatório
- Preço é obrigatório e deve ser maior que zero
- Categoria é obrigatória
- SKU é obrigatório e único

### RN102 - Validação de Código
- Formato: PROD-XXXX (customizável)
- Deve ser único no sistema
- Não pode ser alterado após criação

### RN103 - Validação de SKU
- SKU (Stock Keeping Unit) deve ser único
- Identifica o produto de forma única
- Não pode ser alterado após criação

## 2. Preço e Desconto

### RN104 - Preço Obrigatório
- Preço base deve ser sempre maior que zero
- Preço deve ser definido em reais (BRL)
- Mínimo: R$ 0,01

### RN105 - Desconto (Futuro)
- Desconto percentual deve ser entre 0 e 100%
- Preço com desconto não pode ser negativo
- Desconto válido para período definido

## 3. Estoque

### RN106 - Gestão de Estoque
- Estoque deve ser sempre não-negativo
- Não pode vender quantidade maior que disponível
- Estoque é decrementado automaticamente quando pedido é confirmado

### RN107 - Estoque Mínimo
- Cada produto deve ter estoque mínimo definido
- Sistema alerta quando estoque < estoque mínimo
- Sugestão automática de reposição

### RN108 - Controle de Falta de Estoque
- Produto com estoque zerado não aparece no catálogo
- Produto com baixo estoque aparece como "em falta"
- Cliente não consegue comprar produto sem estoque

## 4. Ativação e Visibilidade

### RN109 - Produto Ativo por Padrão
- Novo produto é criado como inativo (requer ativação)
- Apenas produtos ativos aparecem no catálogo

### RN110 - Visibilidade no Catálogo
- Produtos ativos + com estoque > 0 = visíveis
- Produtos inativos = invisíveis mesmo com estoque
- Produtos sem estoque = fora de catálogo

## 5. Categorização

### RN111 - Categoria Obrigatória
- Todo produto deve estar em uma categoria
- Uma produto pode estar em múltiplas categorias

### RN112 - Categorias Válidas
- Sistema deve manter lista de categorias válidas
- Não pode criar produto com categoria inexistente
- Categorias são definidas pelo admin

## 6. Atualização de Produtos

### RN113 - Dados Atualizáveis
- Nome
- Descrição
- Preço
- Estoque
- Estoque Mínimo
- Categoria
- Imagem
- Status (ativo/inativo)

### RN114 - Dados Não Atualizáveis
- Código (após criação)
- SKU (após criação)
- Data de criação

## 7. Exclusão

### RN115 - Soft Delete
- Produtos não são deletados fisicamente
- Apenas marcados como inativos
- Histórico é preservado

### RN116 - Exclusão com Histórico
- Não pode deletar produto que tem vendas
- Deve ser marcado como inativo
- Histórico de vendas fica ligado ao produto

## 8. Imagens

### RN117 - URL de Imagem Obrigatória
- Todo produto deve ter pelo menos uma imagem
- Imagem deve ser URL válida
- Validar se URL é acessível

## 9. Integração com Pedidos

### RN118 - Validação no Pedido
- Quando pedido é criado, validar:
  - Produto existe
  - Produto está ativo
  - Produto tem estoque suficiente
  - Preço atual está disponível

### RN119 - Cálculo de Total do Pedido
- Total = Σ(quantidade × preço_unitário) - desconto
- Preço unitário é o preço no momento do pedido
- Se preço mudar, pedido futuro usa novo preço

## 10. Auditoria

### RN120 - Rastreamento de Alterações
- Data de criação deve ser registrada
- Data de atualização deve ser registrada
- Quem atualizou (futuro - adicionar user_id)

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
