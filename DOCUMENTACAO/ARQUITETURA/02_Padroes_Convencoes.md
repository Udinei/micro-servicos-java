# 🎯 Padrões e Convenções

## 1. Convenções de Nomenclatura

### Entidades (Models)
```
Nomenclatura: PascalCase
Exemplo: Pedido, ItemPedido, Cliente
Sufixo: Nenhum (apenas o nome da entidade)
```

### DTOs
```
Nomenclatura: PascalCase + DTO
Exemplo: PedidoDTO, NovoPedidoDTO, PedidoResponseDTO
Sufixo: DTO (obrigatório)
Request DTOs: NovoPedidoDTO, AtualizarPedidoDTO
Response DTOs: PedidoDTO, PedidoResponseDTO
```

### Controllers
```
Nomenclatura: PascalCase + Controller
Exemplo: PedidoController, ClienteController
Rota: /api/{recurso}
Sufixo: Controller (obrigatório)
```

### Services
```
Nomenclatura: PascalCase + Service
Exemplo: PedidoService, ClienteService
Sufixo: Service (obrigatório)
```

### Repositories
```
Nomenclatura: PascalCase + Repository
Exemplo: PedidoRepository, ClienteRepository
Sufixo: Repository (obrigatório)
Implementação: JpaRepository<Entity, ID>
```

### Validators
```
Nomenclatura: PascalCase + Validator
Exemplo: PedidoValidator, ClienteValidator
Sufixo: Validator (obrigatório)
```

### Mappers
```
Nomenclatura: PascalCase + Mapper
Exemplo: PedidoMapper, ClienteMapper
Sufixo: Mapper (obrigatório)
Interface com MapStruct
```

## 2. Convenções de Código

### Classes

**Entities (JPA)**
```java
@Entity
@Table(name = "tabela_nome")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NomeDaEntidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
```

**DTOs**
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NomeDaEntidadeDTO {
    private Long id;
    private String nome;
}
```

**Controllers**
```java
@RestController
@RequestMapping("/api/recursos")
@RequiredArgsConstructor
public class RecursoController {
    private final RecursoService service;
    
    @PostMapping
    public ResponseEntity<RecursoDTO> criar(...) { }
}
```

**Services**
```java
@Service
@RequiredArgsConstructor
@Transactional
public class RecursoService {
    private final RecursoRepository repository;
    
    public Recurso salvar(Recurso recurso) { }
}
```

### Métodos

**Naming Convention**
- GET: `buscar*`, `listar*`, `obter*`
- POST: `criar*`, `salvar*`, `registrar*`
- PUT: `atualizar*`, `editar*`
- DELETE: `deletar*`, `remover*`, `cancelar*`
- PATCH: `alterar*`

**Exemplo**
```java
// Controllers
@GetMapping
public ResponseEntity<List<PedidoDTO>> listarTodos() { }

@GetMapping("/{id}")
public ResponseEntity<PedidoDTO> buscarPorId(@PathVariable Long id) { }

@PostMapping
public ResponseEntity<PedidoDTO> criar(@RequestBody NovoPedidoDTO dto) { }

@PutMapping("/{id}")
public ResponseEntity<PedidoDTO> atualizar(@PathVariable Long id, @RequestBody AtualizarPedidoDTO dto) { }

@DeleteMapping("/{id}")
public ResponseEntity<Void> deletar(@PathVariable Long id) { }

// Services
@Transactional
public Pedido salvar(Pedido pedido) { }

@Transactional(readOnly = true)
public Pedido buscarPorCodigo(Long codigo) { }

@Transactional
public void deletar(Long codigo) { }
```

## 3. Estrutura de Pacotes

```
src/main/java/com/github/udinei/icompras/
├── pedidos/
│   ├── controller/          (Controllers REST)
│   ├── service/             (Lógica de negócio)
│   ├── repository/          (Acesso a dados)
│   ├── model/               (Entidades JPA)
│   ├── dto/                 (Data Transfer Objects)
│   ├── mapper/              (Mapeadores)
│   ├── validator/           (Validadores)
│   ├── exception/           (Exceções customizadas)
│   └── client/              (Clientes HTTP)
├── clientes/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   ├── dto/
│   ├── mapper/
│   ├── validator/
│   └── exception/
├── produtos/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   ├── dto/
│   ├── mapper/
│   ├── validator/
│   └── exception/
└── config/                  (Configurações globais)
```

## 4. Anotações Spring

### Controllers
```java
@RestController              // Indica que é um controller REST
@RequestMapping("/api/...")  // Define a rota base
@PostMapping                 // HTTP POST
@GetMapping                  // HTTP GET
@PutMapping                  // HTTP PUT
@DeleteMapping               // HTTP DELETE
@PathVariable                // Extrai variável da URL
@RequestBody                 // Corpo da requisição
@RequestParam                // Parâmetro de query string
```

### Services
```java
@Service                     // Indica service
@Transactional              // Gerencia transação
@RequiredArgsConstructor    // Lombok gera constructor com dependências
```

### Repositories
```java
@Repository                 // Indica repository (opcional com JpaRepository)
```

### Validação
```java
@Valid                      // Valida anotações de validação
@NotNull                    // Campo não pode ser nulo
@NotBlank                   // String não pode ser vazia
@Size(min=3, max=100)      // Tamanho válido
@Email                      // Formato de email
@Pattern                    // Regex validation
```

## 5. Padrão de Resposta

### Sucesso (2xx)
```json
{
  "id": 1,
  "nome": "João",
  "email": "joao@example.com"
}
```

### Erro (4xx, 5xx)
```json
{
  "erro": "CLIENTE_NAO_ENCONTRADO",
  "mensagem": "Cliente com ID 999 não foi encontrado",
  "campo": "id",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## 6. Transações

### Leitura
```java
@Transactional(readOnly = true)
public List<Pedido> listarTodos() {
    return repository.findAll();
}
```

### Escrita
```java
@Transactional
public Pedido salvar(Pedido pedido) {
    return repository.save(pedido);
}
```

## 7. Logging

### Padrão
```java
private static final Logger logger = LoggerFactory.getLogger(NomeClass.class);

logger.info("Pedido criado: {}", pedido.getId());
logger.warn("Estoque baixo para produto: {}", produto.getId());
logger.error("Erro ao processar pagamento", exception);
```

## 8. Tratamento de Exceções

### Customizadas
```java
public class ClienteNaoEncontradoException extends RuntimeException {
    public ClienteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
```

### Handler Global
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleClienteNaoEncontrado(...) {
        // Retorna erro formatado
    }
}
```

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
