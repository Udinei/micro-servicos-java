# 🎯 Entidades Principais

## 1. Entidade Cliente

### Classe JPA
```java
@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 255)
    @Column(nullable = false)
    private String nome;
    
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    @Column(nullable = false, unique = true)
    private String email;
    
    @NotBlank(message = "CPF é obrigatório")
    @Column(nullable = false, unique = true)
    private String cpf;
    
    @Pattern(regexp = "\\(\\d{2}\\)\\s?9\\d{4}-\\d{4}|\\(\\d{2}\\)\\s?\\d{4}-\\d{4}")
    private String telefone;
    
    @Size(min = 5, max = 255)
    private String endereco;
    
    private String cidade;
    private String estado;
    private String cep;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime dataAtualizacao = LocalDateTime.now();
    
    // Relacionamentos
    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos = new ArrayList<>();
}
```

### DTO Request
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NovoClienteDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 255)
    private String nome;
    
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    private String email;
    
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;
    
    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;
    
    @NotBlank(message = "Endereço é obrigatório")
    @Size(min = 5)
    private String endereco;
    
    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;
    
    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2)
    private String estado;
    
    @NotBlank(message = "CEP é obrigatório")
    private String cep;
}
```

### DTO Response
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
}
```

---

## 2. Entidade Produto

### Classe JPA
```java
@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Código é obrigatório")
    @Column(nullable = false, unique = true)
    private String codigo;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 255)
    @Column(nullable = false)
    private String nome;
    
    @Size(min = 10, max = 2000)
    private String descricao;
    
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    @Column(nullable = false)
    private BigDecimal preco;
    
    @NotBlank(message = "Categoria é obrigatória")
    @Column(nullable = false)
    private String categoria;
    
    @NotBlank(message = "SKU é obrigatório")
    @Column(nullable = false, unique = true)
    private String sku;
    
    @NotNull(message = "Estoque é obrigatório")
    @Min(value = 0, message = "Estoque não pode ser negativo")
    @Column(nullable = false)
    private Integer estoque;
    
    @NotNull(message = "Estoque mínimo é obrigatório")
    @Min(value = 1)
    @Column(nullable = false)
    private Integer estoqueMinimo;
    
    @URL(message = "Imagem deve ser uma URL válida")
    private String imagemUrl;
    
    @Column(nullable = false)
    private Boolean ativo = false;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime dataAtualizacao = LocalDateTime.now();
    
    // Relacionamentos
    @OneToMany(mappedBy = "produto")
    private List<ItemPedido> itens = new ArrayList<>();
}
```

### DTO Request
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NovoProdutoDTO {
    
    @NotBlank(message = "Código é obrigatório")
    private String codigo;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3)
    private String nome;
    
    @Size(min = 10)
    private String descricao;
    
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01")
    private BigDecimal preco;
    
    @NotBlank(message = "Categoria é obrigatória")
    private String categoria;
    
    @NotBlank(message = "SKU é obrigatório")
    private String sku;
    
    @NotNull(message = "Estoque é obrigatório")
    @Min(value = 0)
    private Integer estoque;
    
    @NotNull(message = "Estoque mínimo é obrigatório")
    @Min(value = 1)
    private Integer estoqueMinimo;
    
    @URL
    private String imagemUrl;
}
```

---

## 3. Entidade Pedido

### Classe JPA
```java
@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false, unique = true)
    private String codigo;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataPedido = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status = StatusPedido.PENDENTE;
    
    @NotNull
    @DecimalMin(value = "0.01")
    @Column(nullable = false)
    private BigDecimal total;
    
    private String observacoes;
    
    @Enumerated(EnumType.STRING)
    private TipoPagamento tipoPagamento;
    
    private String chavePagamento;
    private String codigoRastreio;
    private String urlNf;
    
    private LocalDateTime dataEntregaEstimada;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime dataAtualizacao = LocalDateTime.now();
    
    // Relacionamentos
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();
    
    // Métodos auxiliares
    public void addItem(ItemPedido item) {
        this.itens.add(item);
        item.setPedido(this);
    }
    
    public void clearItens() {
        this.itens.clear();
    }
}
```

### DTO Request
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NovoPedidoDTO {
    
    @NotNull(message = "Cliente ID é obrigatório")
    private Long clienteId;
    
    @NotEmpty(message = "Pedido deve ter pelo menos um item")
    @Size(min = 1, max = 100)
    private List<ItemPedidoDTO> itens;
    
    private String observacoes;
    
    @NotNull(message = "Tipo de pagamento é obrigatório")
    private TipoPagamento tipoPagamento;
}
```

### DTO Response
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    
    private Long id;
    private String codigo;
    private Long clienteId;
    private String clienteNome;
    private LocalDateTime dataPedido;
    private StatusPedido status;
    private List<ItemPedidoDTO> itens;
    private BigDecimal total;
    private String observacoes;
    private TipoPagamento tipoPagamento;
    private String chavePagamento;
    private String codigoRastreio;
    private String urlNf;
    private LocalDateTime dataEntregaEstimada;
    private LocalDateTime dataCriacao;
}
```

---

## 4. Entidade ItemPedido

### Classe JPA
```java
@Entity
@Table(name = "itens_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @NotNull
    @Min(value = 1)
    @Column(nullable = false)
    private Integer quantidade;
    
    @NotNull
    @DecimalMin(value = "0.01")
    @Column(nullable = false)
    private BigDecimal precoUnitario;
    
    @NotNull
    @Column(nullable = false)
    private BigDecimal subtotal;
    
    // Métodos auxiliares
    public void calcularSubtotal() {
        this.subtotal = precoUnitario.multiply(
            new BigDecimal(quantidade)
        );
    }
}
```

### DTO Request
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO {
    
    @NotNull(message = "Produto ID é obrigatório")
    private Long produtoId;
    
    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que 0")
    private Integer quantidade;
}
```

---

## 5. Enums

### StatusPedido
```java
public enum StatusPedido {
    PENDENTE("Aguardando confirmação"),
    CONFIRMADO("Pedido confirmado"),
    PROCESSANDO("Sendo separado"),
    ENVIADO("Em trânsito"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado"),
    DEVOLVIDO("Devolvido");
    
    private final String descricao;
    
    StatusPedido(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
```

### TipoPagamento
```java
public enum TipoPagamento {
    BOLETO("Boleto"),
    CARTAO_CREDITO("Cartão de Crédito"),
    PIX("PIX"),
    TRANSFERENCIA_BANCARIA("Transferência Bancária");
    
    private final String descricao;
    
    TipoPagamento(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
```

---

**Versão:** 1.0.0  
**Data:** 22/01/2026
