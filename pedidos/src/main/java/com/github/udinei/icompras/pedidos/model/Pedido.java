package com.github.udinei.icompras.pedidos.model;

import com.github.udinei.icompras.pedidos.dto.DadosPagamentoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "codigo_cliente", nullable = false)
    private Long codigoCliente;

    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido;

    // Dados de pagamento
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pagamento", length = 30)
    private TipoPagamento tipoPagamento;

    @Column(name = "chave_pix")
    private String chavePix;

    @Column(name = "numero_cartao")
    private String numeroCartao;

    @Column(name = "codigo_autorizacao")
    private String codigoAutorizacao;

    @Column(name = "linha_digitavel")
    private String linhaDigitavel;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusPedido status;

    @Column(nullable = false, precision = 16, scale = 2)
    private BigDecimal total;

    @Column(name = "codigo_rastreio", length = 255)
    private String codigoRastreio;

    @Column(name = "url_nf", columnDefinition = "TEXT")
    private String urlNf;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemPedido> itens = new ArrayList<>();

    // Atributo transient para transferir dados de pagamento (não persistido no banco)
    @Transient
    private DadosPagamentoDTO dadosPagamento;

    @Column(name = "chave_pagamento")
    private String chavePagamento;

    @PrePersist
    protected void onCreate() {
        if (dataPedido == null) {
            dataPedido = LocalDateTime.now();
        }
    }

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
}
