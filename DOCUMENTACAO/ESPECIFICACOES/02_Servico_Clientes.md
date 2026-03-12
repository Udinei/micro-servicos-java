# 👥 Especificação - Serviço de Clientes

## 1. Funcionalidades

### 1.1 Cadastro de Clientes
- Criar novo cliente
- Validar CPF/CNPJ
- Registrar dados básicos

### 1.2 Consulta
- Listar todos os clientes
- Buscar por ID
- Buscar por CPF/CNPJ

### 1.3 Atualização
- Editar dados pessoais
- Atualizar endereço
- Alterar informações de contato

### 1.4 Exclusão
- Deletar cliente (soft delete recomendado)

## 2. Endpoints da API

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/clientes` | Criar novo cliente |
| GET | `/api/clientes` | Listar todos |
| GET | `/api/clientes/{id}` | Buscar por ID |
| PUT | `/api/clientes/{id}` | Atualizar cliente |
| DELETE | `/api/clientes/{id}` | Deletar cliente |
| GET | `/api/clientes/cpf/{cpf}` | Buscar por CPF |

## 3. Modelo de Dados

```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "telefone": "(11) 99999-9999",
  "cpf": "123.456.789-00",
  "endereco": "Rua A, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "ativo": true,
  "dataCadastro": "2024-01-15T10:30:00Z"
}
```

## 4. Validações

- **Nome**: Obrigatório, mínimo 3 caracteres
- **Email**: Formato válido, único no sistema
- **CPF**: Formato válido (XXX.XXX.XXX-XX), único
- **Telefone**: Formato válido
- **Endereço**: Obrigatório, mínimo 5 caracteres
- **CEP**: Formato válido (XXXXX-XXX)

## 5. Códigos HTTP de Resposta

| Código | Descrição |
|--------|-----------|
| 201 | Cliente criado com sucesso |
| 200 | Operação bem-sucedida |
| 400 | Dados inválidos |
| 404 | Cliente não encontrado |
| 409 | CPF/Email já cadastrado |
| 500 | Erro interno do servidor |

## 6. DTOs

### Request - Novo Cliente
```json
{
  "nome": "João Silva",
  "email": "joao@example.com",
  "telefone": "(11) 99999-9999",
  "cpf": "123.456.789-00",
  "endereco": "Rua A, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567"
}
```

### Response - Cliente
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "telefone": "(11) 99999-9999",
  "cpf": "123.456.789-00",
  "endereco": "Rua A, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "ativo": true,
  "dataCadastro": "2024-01-15T10:30:00Z"
}
```

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
