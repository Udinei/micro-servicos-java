package com.github.udinei.icompras.pedidos.dto;

import com.github.udinei.icompras.pedidos.model.StatusPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record NovoPedidoDTO(
        @NotNull(message = "Código do cliente é obrigatório")
        Long codigoCliente,
        
        LocalDateTime dataPedido,
        
        @Valid
        DadosPagamentoDTO dadosPagamento,
        
        String observacoes,
        
        StatusPedido status,
        
        @NotNull(message = "Total é obrigatório")
        BigDecimal total,
        
        String codigoRastreio,
        
        String urlNf,
        
        String chavePagamento,
        
        @NotEmpty(message = "Pedido deve conter pelo menos um item")
        @Valid
        List<NovoItemPedidoDTO> itens
) {
}
