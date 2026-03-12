# 📦 Especificação - Serviço de Produtos

## 1. Funcionalidades

### 1.1 Gerenciamento de Produtos
- Criar novo produto
- Atualizar informações
- Consultar catálogo
- Deletar produto

### 1.2 Categorização
- Organizar por categoria
- Subcategorias
- Tags

### 1.3 Inventário
- Controlar estoque
- Atualizar quantidade
- Alertar quando em falta

### 1.4 Busca e Filtros
- Busca por nome
- Filtro por categoria
- Filtro por preço
- Ordenação

## 2. Endpoints da API

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/produtos` | Criar novo produto |
| GET | `/api/produtos` | Listar todos |
| GET | `/api/produtos/{id}` | Buscar por ID |
| PUT | `/api/produtos/{id}` | Atualizar produto |
| DELETE | `/api/produtos/{id}` | Deletar produto |
| GET | `/api/produtos/categoria/{categoria}` | Buscar por categoria |
| GET | `/api/produtos/busca?termo=xyz` | Buscar por termo |

## 3. Modelo de Dados

```json
{
  "id": 1,
  "codigo": "PROD-001",
  "nome": "Notebook Dell",
  "descricao": "Notebook de alta performance",
  "preco": 3500.00,
  "categoria": "Eletrônicos",
  "sku": "NB-DELL-15-2024",
  "estoque": 50,
  "estoque_minimo": 10,
  "imagem_url": "https://example.com/imagens/produto.jpg",
  "ativo": true,
  "data_criacao": "2024-01-10T14:30:00Z",
  "data_atualizacao": "2024-01-20T09:15:00Z"
}
```

## 4. Validações

- **Código**: Obrigatório, único
- **Nome**: Obrigatório, mínimo 3 caracteres
- **Descrição**: Mínimo 10 caracteres
- **Preço**: Obrigatório, maior que zero
- **Categoria**: Obrigatório
- **SKU**: Único, formato válido
- **Estoque**: Não pode ser negativo
- **Estoque Mínimo**: Menor que estoque total

## 5. Códigos HTTP de Resposta

| Código | Descrição |
|--------|-----------|
| 201 | Produto criado com sucesso |
| 200 | Operação bem-sucedida |
| 400 | Dados inválidos |
| 404 | Produto não encontrado |
| 409 | Código/SKU já existe |
| 422 | Validação falhou |
| 500 | Erro interno |

## 6. DTOs

### Request - Novo Produto
```json
{
  "codigo": "PROD-001",
  "nome": "Notebook Dell",
  "descricao": "Notebook de alta performance",
  "preco": 3500.00,
  "categoria": "Eletrônicos",
  "sku": "NB-DELL-15-2024",
  "estoque": 50,
  "estoque_minimo": 10,
  "imagem_url": "https://example.com/imagens/produto.jpg"
}
```

### Response - Produto
```json
{
  "id": 1,
  "codigo": "PROD-001",
  "nome": "Notebook Dell",
  "descricao": "Notebook de alta performance",
  "preco": 3500.00,
  "categoria": "Eletrônicos",
  "sku": "NB-DELL-15-2024",
  "estoque": 50,
  "estoque_minimo": 10,
  "imagem_url": "https://example.com/imagens/produto.jpg",
  "ativo": true,
  "data_criacao": "2024-01-10T14:30:00Z",
  "data_atualizacao": "2024-01-20T09:15:00Z"
}
```

## 7. Fluxo de Disponibilidade

```
Produto Criado
    ↓
Ativo = true → Disponível para venda
    ↓
Estoque > 0 → Pode ser adicionado ao carrinho
    ↓
Estoque < Estoque Mínimo → Alerta de reposição
    ↓
Estoque = 0 → Fora de estoque (visível mas não comprável)
    ↓
Ativo = false → Não aparece no catálogo
```

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
