package com.github.udinei.icompras.pedidos.dto;

import com.github.udinei.icompras.pedidos.model.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoDTO(
        Long codigo,
        Long codigoCliente,
        LocalDateTime dataPedido,
        DadosPagamentoDTO dadosPagamento,
        String observacoes,
        StatusPedido status,
        BigDecimal total,
        String codigoRastreio,
        String urlNf,
        String chavePagamento,
        List<ItemPedidoDTO> itens
) {
}
