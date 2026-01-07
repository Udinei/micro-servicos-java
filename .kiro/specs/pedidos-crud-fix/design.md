# Design Document - Correção do CRUD de Pedidos

## Overview

Este documento descreve o design técnico para implementar corretamente o CRUD do microsserviço de pedidos. O design foca em corrigir problemas de persistência JPA, relacionamentos bidirecionais entre entidades, e implementar uma suíte completa de testes.

## Architecture

O microsserviço de pedidos segue uma arquitetura em camadas:

```
┌─────────────────────────────────────┐
│     PedidoController (REST API)     │
│  - Endpoints HTTP                   │
│  - Validação de entrada             │
│  - Mapeamento de respostas          │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│       PedidoService (Business)      │
│  - Lógica de negócio                │
│  - Gerenciamento de transações      │
│  - Associação de relacionamentos    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│   PedidoRepository (Data Access)    │
│  - Operações JPA                    │
│  - Queries customizadas             │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         Database (PostgreSQL)       │
│  - Tabela pedido                    │
│  - Tabela item_pedido               │
└─────────────────────────────────────┘
```

## Components and Interfaces

### 1. Entidades (Model Layer)

#### Pedido
```java
@Entity
@Table(name = "pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    
    private Long codigoCliente;
    private LocalDateTime dataPedido;
    private String chavePagamento;
    private String observacoes;
    
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    
    private BigDecimal total;
    private String codigoRastreio;
    private String urlNf;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();
    
    // Métodos auxiliares para gerenciar relacionamento bidirecional
    public void addItem(ItemPedido item);
    public void removeItem(ItemPedido item);
    public void clearItens();
}
```

#### ItemPedido
```java
@Entity
@Table(name = "item_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_pedido", nullable = false)
    @JsonIgnore
    private Pedido pedido;
    
    private Long codigoProduto;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    
    public BigDecimal getSubtotal();
}
```

### 2. Repository Layer

```java
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByCodigoCliente(Long codigoCliente);
    List<Pedido> findByStatus(StatusPedido status);
}
```

### 3. Service Layer

```java
@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    
    @Transactional(readOnly = true)
    public List<Pedido> listarTodos();
    
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorCodigo(Long codigo);
    
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorCliente(Long codigoCliente);
    
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorStatus(StatusPedido status);
    
    @Transactional
    public Pedido salvar(Pedido pedido);
    
    @Transactional
    public Pedido atualizar(Long codigo, Pedido pedidoAtualizado);
    
    @Transactional
    public Pedido atualizarStatus(Long codigo, StatusPedido novoStatus);
    
    @Transactional
    public void deletar(Long codigo);
    
    @Transactional(readOnly = true)
    public boolean existe(Long codigo);
}
```

### 4. Controller Layer

```java
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;
    
    @GetMapping
    ResponseEntity<List<Pedido>> listarTodos();
    
    @GetMapping("/{codigo}")
    ResponseEntity<Pedido> buscarPorCodigo(@PathVariable Long codigo);
    
    @GetMapping("/cliente/{codigoCliente}")
    ResponseEntity<List<Pedido>> buscarPorCliente(@PathVariable Long codigoCliente);
    
    @GetMapping("/status/{status}")
    ResponseEntity<List<Pedido>> buscarPorStatus(@PathVariable StatusPedido status);
    
    @PostMapping
    ResponseEntity<Pedido> criar(@RequestBody Pedido pedido);
    
    @PutMapping("/{codigo}")
    ResponseEntity<Pedido> atualizar(@PathVariable Long codigo, @RequestBody Pedido pedido);
    
    @PatchMapping("/{codigo}/status")
    ResponseEntity<Pedido> atualizarStatus(@PathVariable Long codigo, @RequestParam StatusPedido status);
    
    @DeleteMapping("/{codigo}")
    ResponseEntity<Void> deletar(@PathVariable Long codigo);
}
```

## Data Models

### Database Schema

```sql
CREATE TABLE pedido (
    codigo BIGSERIAL PRIMARY KEY,
    codigo_cliente BIGINT NOT NULL,
    data_pedido TIMESTAMP NOT NULL,
    chave_pagamento VARCHAR(255),
    observacoes TEXT,
    status VARCHAR(20),
    total DECIMAL(16,2) NOT NULL,
    codigo_rastreio VARCHAR(255),
    url_nf TEXT
);

CREATE TABLE item_pedido (
    codigo BIGSERIAL PRIMARY KEY,
    codigo_pedido BIGINT NOT NULL,
    codigo_produto BIGINT NOT NULL,
    quantidade INTEGER NOT NULL,
    valor_unitario DECIMAL(16,2) NOT NULL,
    FOREIGN KEY (codigo_pedido) REFERENCES pedido(codigo) ON DELETE CASCADE
);
```

### Relacionamentos

- **Pedido → ItemPedido**: One-to-Many com cascade ALL e orphanRemoval
- **ItemPedido → Pedido**: Many-to-One com fetch LAZY


## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system-essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

### Property 1: Bidirectional relationship establishment
*For any* Pedido and any set of ItemPedido instances, when items are added to the pedido using the addItem method, each item should have its pedido reference set to the parent pedido.
**Validates: Requirements 2.1, 2.3**

### Property 2: Cascade persistence
*For any* Pedido with any number of ItemPedido instances, when the pedido is saved, all associated items should be persisted to the database with generated IDs.
**Validates: Requirements 2.2, 3.1, 3.2**

### Property 3: Orphan removal on item removal
*For any* Pedido with ItemPedido instances, when an item is removed from the pedido and the pedido is saved, the removed item should no longer exist in the database.
**Validates: Requirements 2.4**

### Property 4: Default timestamp assignment
*For any* Pedido created without a dataPedido value, when the pedido is persisted, the dataPedido should be automatically set to a non-null timestamp.
**Validates: Requirements 3.4**

### Property 5: Update replaces items
*For any* existing Pedido, when it is updated with a new set of ItemPedido instances, the old items should be replaced with the new items and the bidirectional relationship should be maintained.
**Validates: Requirements 4.2, 4.3**

### Property 6: Orphan removal on update
*For any* Pedido being updated, when ItemPedido instances are removed from the items list, those orphaned items should be deleted from the database.
**Validates: Requirements 4.4**

### Property 7: Complete retrieval
*For any* saved Pedido with ItemPedido instances, when the pedido is retrieved by codigo, all associated items should be included in the returned entity.
**Validates: Requirements 5.1**

### Property 8: List includes all items
*For any* set of saved Pedidos, when all pedidos are listed, each pedido should include all its associated ItemPedido instances.
**Validates: Requirements 5.2**

### Property 9: Client filtering accuracy
*For any* codigoCliente and any set of Pedidos, when pedidos are filtered by that codigoCliente, all returned pedidos should have that codigoCliente and no pedidos with different codigoCliente should be returned.
**Validates: Requirements 5.3**

### Property 10: Status filtering accuracy
*For any* StatusPedido and any set of Pedidos, when pedidos are filtered by that status, all returned pedidos should have that status and no pedidos with different status should be returned.
**Validates: Requirements 5.4**

### Property 11: Cascade deletion
*For any* Pedido with ItemPedido instances, when the pedido is deleted, all associated items should also be removed from the database.
**Validates: Requirements 6.1**

## Error Handling

### Service Layer Exceptions

1. **EntityNotFoundException**: Thrown when attempting to update or retrieve a non-existent Pedido
   - Mapped to HTTP 404 in controller layer

2. **DataIntegrityViolationException**: Thrown when database constraints are violated
   - Mapped to HTTP 400 in controller layer

3. **TransactionException**: Thrown when transaction management fails
   - Mapped to HTTP 500 in controller layer

### Controller Layer Error Responses

```java
@ExceptionHandler(EntityNotFoundException.class)
public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(ex.getMessage()));
}

@ExceptionHandler(DataIntegrityViolationException.class)
public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("Invalid data provided"));
}
```

## Testing Strategy

### Unit Testing

Unit tests will focus on the PedidoService layer using Mockito to mock the repository:

- **Test Scope**: Individual service methods
- **Framework**: JUnit 5 + Mockito
- **Coverage Goals**: All service methods, edge cases, error scenarios

Key test scenarios:
- Saving pedido with items
- Updating pedido and replacing items
- Querying pedidos by different criteria
- Deleting pedidos
- Error handling for non-existent entities

### Integration Testing

Integration tests will verify the REST API endpoints using MockMvc:

- **Test Scope**: Controller endpoints with mocked service layer
- **Framework**: Spring Boot Test + MockMvc
- **Coverage Goals**: All HTTP endpoints, request/response mapping, status codes

Key test scenarios:
- POST /api/pedidos - Create pedido
- GET /api/pedidos - List all pedidos
- GET /api/pedidos/{codigo} - Get pedido by ID
- GET /api/pedidos/cliente/{codigoCliente} - Filter by client
- GET /api/pedidos/status/{status} - Filter by status
- PUT /api/pedidos/{codigo} - Update pedido
- PATCH /api/pedidos/{codigo}/status - Update status
- DELETE /api/pedidos/{codigo} - Delete pedido

### Property-Based Testing

Property-based tests will verify universal properties using JUnit-Quickcheck:

- **Test Scope**: Correctness properties defined in this document
- **Framework**: JUnit-Quickcheck
- **Iterations**: Minimum 100 per property
- **Coverage Goals**: All correctness properties

Each property-based test will:
1. Generate random valid inputs (Pedido and ItemPedido instances)
2. Execute the operation under test
3. Verify the property holds true
4. Be tagged with the format: `**Feature: pedidos-crud-fix, Property {number}: {property_text}**`

### Test Data Generation

For property-based testing, we will create generators for:

```java
@Generator
public class PedidoGenerator extends Generator<Pedido> {
    // Generate random valid Pedido instances
}

@Generator
public class ItemPedidoGenerator extends Generator<ItemPedido> {
    // Generate random valid ItemPedido instances
}
```

## Implementation Notes

### Lombok Configuration

Ensure Lombok is properly configured in pom.xml:

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>provided</scope>
</dependency>
```

Use `@Getter` and `@Setter` instead of `@Data` to avoid issues with bidirectional relationships and equals/hashCode.

### JPA Best Practices

1. Use `@Transactional(readOnly = true)` for query methods
2. Use `@Transactional` for write operations
3. Manage bidirectional relationships explicitly with helper methods
4. Use `orphanRemoval = true` for proper cascade deletion
5. Use `FetchType.LAZY` for associations to avoid N+1 queries

### Bidirectional Relationship Management

The Pedido entity should provide helper methods to maintain consistency:

```java
public void addItem(ItemPedido item) {
    itens.add(item);
    item.setPedido(this);
}

public void removeItem(ItemPedido item) {
    itens.remove(item);
    item.setPedido(null);
}

public void clearItens() {
    itens.forEach(item -> item.setPedido(null));
    itens.clear();
}
```

### Update Operation Strategy

For the update operation, we need to:
1. Clear existing items
2. Add new items using the helper method
3. Let JPA handle orphan removal

```java
@Transactional
public Pedido atualizar(Long codigo, Pedido pedidoAtualizado) {
    return pedidoRepository.findById(codigo)
        .map(pedido -> {
            // Update simple properties
            pedido.setCodigoCliente(pedidoAtualizado.getCodigoCliente());
            // ... other properties
            
            // Replace items
            pedido.clearItens();
            pedidoAtualizado.getItens().forEach(pedido::addItem);
            
            return pedidoRepository.save(pedido);
        })
        .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
}
```
