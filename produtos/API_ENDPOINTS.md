# API Endpoints - Produtos

Base URL: `http://localhost:8081/api/produtos`

**Nota:** A aplicação está rodando na porta **8081** (não 8082)

## Endpoints Disponíveis

### 1. Listar todos os produtos
```http
GET http://localhost:8082/api/produtos
```

**Exemplo com curl:**
```bash
curl -X GET http://localhost:8082/api/produtos
```

---

### 2. Buscar produto por código
```http
GET http://localhost:8082/api/produtos/{codigo}
```

**Exemplo com curl:**
```bash
curl -X GET http://localhost:8082/api/produtos/1
```

**Resposta (200 OK):**
```json
{
  "codigo": 1,
  "nome": "Notebook Dell",
  "valorUnitario": 3500.00
}
```

**Resposta (404 Not Found):** Produto não encontrado

---

### 3. Buscar produtos por nome
```http
GET http://localhost:8082/api/produtos/buscar?nome={nome}
```

**Exemplo com curl:**
```bash
curl -X GET "http://localhost:8082/api/produtos/buscar?nome=notebook"
```

**Resposta (200 OK):**
```json
[
  {
    "codigo": 1,
    "nome": "Notebook Dell",
    "valorUnitario": 3500.00
  },
  {
    "codigo": 2,
    "nome": "Notebook HP",
    "valorUnitario": 2800.00
  }
]
```

---

### 4. Criar novo produto
```http
POST http://localhost:8082/api/produtos
Content-Type: application/json
```

**Body:**
```json
{
  "nome": "Mouse Logitech",
  "valorUnitario": 89.90
}
```

**Exemplo com curl:**
```bash
curl -X POST http://localhost:8082/api/produtos \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Mouse Logitech\",\"valorUnitario\":89.90}"
```

**Resposta (201 Created):**
```json
{
  "codigo": 3,
  "nome": "Mouse Logitech",
  "valorUnitario": 89.90
}
```

---

### 5. Atualizar produto existente
```http
PUT http://localhost:8082/api/produtos/{codigo}
Content-Type: application/json
```

**Body:**
```json
{
  "nome": "Mouse Logitech MX Master",
  "valorUnitario": 299.90
}
```

**Exemplo com curl:**
```bash
curl -X PUT http://localhost:8082/api/produtos/3 \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Mouse Logitech MX Master\",\"valorUnitario\":299.90}"
```

**Resposta (200 OK):**
```json
{
  "codigo": 3,
  "nome": "Mouse Logitech MX Master",
  "valorUnitario": 299.90
}
```

**Resposta (404 Not Found):** Produto não encontrado

---

### 6. Deletar produto
```http
DELETE http://localhost:8082/api/produtos/{codigo}
```

**Exemplo com curl:**
```bash
curl -X DELETE http://localhost:8082/api/produtos/3
```

**Resposta (204 No Content):** Produto deletado com sucesso

**Resposta (404 Not Found):** Produto não encontrado

---

## Testando com PowerShell

### Listar todos
```powershell
Invoke-RestMethod -Uri "http://localhost:8082/api/produtos" -Method Get
```

### Buscar por código
```powershell
Invoke-RestMethod -Uri "http://localhost:8082/api/produtos/1" -Method Get
```

### Criar produto
```powershell
$body = @{
    nome = "Teclado Mecânico"
    valorUnitario = 450.00
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8082/api/produtos" `
    -Method Post `
    -Body $body `
    -ContentType "application/json"
```

### Atualizar produto
```powershell
$body = @{
    nome = "Teclado Mecânico RGB"
    valorUnitario = 550.00
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8082/api/produtos/1" `
    -Method Put `
    -Body $body `
    -ContentType "application/json"
```

### Deletar produto
```powershell
Invoke-RestMethod -Uri "http://localhost:8082/api/produtos/1" -Method Delete
```

---

## Testando com Postman

1. Importe a collection ou crie manualmente
2. Configure a base URL: `http://localhost:8082/api/produtos`
3. Use os exemplos acima para cada endpoint

---

## Códigos de Status HTTP

| Código | Descrição |
|--------|-----------|
| 200 OK | Requisição bem-sucedida |
| 201 Created | Recurso criado com sucesso |
| 204 No Content | Recurso deletado com sucesso |
| 404 Not Found | Recurso não encontrado |
| 500 Internal Server Error | Erro no servidor |

---

## Executar a aplicação

```bash
cd produtos
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8082`
