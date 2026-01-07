package com.github.udinei.icompras.pedidos.dto;

import com.github.udinei.icompras.pedidos.model.TipoPagamento;
import jakarta.validation.constraints.NotNull;

public record DadosPagamentoDTO(
        @NotNull(message = "Tipo de pagamento é obrigatório")
        TipoPagamento tipoPagamento,
        
        String chavePix,
        String numeroCartao,
        String codigoAutorizacao,
        String linhaDigitavel
) {
}
