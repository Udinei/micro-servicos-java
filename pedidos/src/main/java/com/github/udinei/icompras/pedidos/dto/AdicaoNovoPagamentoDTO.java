package com.github.udinei.icompras.pedidos.dto;

import com.github.udinei.icompras.pedidos.model.TipoPagamento;

public record AdicaoNovoPagamentoDTO(
        Long codigoPedido, String dados, TipoPagamento tipoPagamento){

}
