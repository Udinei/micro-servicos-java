package com.github.udinei.icompras.pedidos.client;

import com.github.udinei.icompras.pedidos.model.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class ServicoBancarioClient {

    public String solicitarPagamento(Pedido pedido) {
        Long codigo = pedido != null ? pedido.getCodigo() : null;

        log.info("Solicitando pagamento para pedido codigo={}", codigo);
        String idPagamento = UUID.randomUUID().toString();

        
        log.info("Pagamento solicitado com id={}", idPagamento);
        return idPagamento;
    }

}
