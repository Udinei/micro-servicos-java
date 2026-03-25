package com.github.udinei.icompras.pedidos.dto;

public record RecebimentoCallBackPagamentoDTO(
        Long codigo,
        String chavePagamento,
        boolean status,
        String observacoes) {
}
