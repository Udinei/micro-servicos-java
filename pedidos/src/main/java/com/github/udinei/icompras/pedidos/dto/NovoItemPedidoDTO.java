package com.github.udinei.icompras.pedidos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record NovoItemPedidoDTO(
        @NotNull(message = "Código do produto é obrigatório")
        Long codigoProduto,
        
        @NotNull(message = "Quantidade é obrigatória")
        @Min(value = 1, message = "Quantidade deve ser maior que zero")
        Integer quantidade,
        
        @NotNull(message = "Valor unitário é obrigatório")
        @Min(value = 0, message = "Valor unitário deve ser positivo")
        BigDecimal valorUnitario
) {
}
