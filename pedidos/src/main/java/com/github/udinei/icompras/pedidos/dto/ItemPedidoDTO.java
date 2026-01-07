package com.github.udinei.icompras.pedidos.dto;

import java.math.BigDecimal;

public record ItemPedidoDTO(
        Long codigo,
        Long codigoProduto,
        Integer quantidade,
        BigDecimal valorUnitario,
        BigDecimal subtotal
) {
}
