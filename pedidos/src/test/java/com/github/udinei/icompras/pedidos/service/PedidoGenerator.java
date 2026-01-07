package com.github.udinei.icompras.pedidos.service;

import com.github.udinei.icompras.pedidos.model.Pedido;
import com.github.udinei.icompras.pedidos.model.StatusPedido;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PedidoGenerator extends Generator<Pedido> {

    public PedidoGenerator() {
        super(Pedido.class);
    }

    @Override
    public Pedido generate(SourceOfRandomness random, GenerationStatus status) {
        Pedido pedido = new Pedido();
        
        // Generate random values
        pedido.setCodigoCliente(random.nextLong(1, 10000));
        pedido.setDataPedido(LocalDateTime.now().minusDays(random.nextInt(0, 365)));
        pedido.setObservacoes(random.nextBoolean() ? "Observação " + random.nextInt(1, 100) : null);
        
        // Random status
        StatusPedido[] statuses = StatusPedido.values();
        pedido.setStatus(statuses[random.nextInt(0, statuses.length - 1)]);
        
        // Random total between 10.00 and 10000.00
        pedido.setTotal(BigDecimal.valueOf(random.nextDouble(10.0, 10000.0))
                                  .setScale(2, BigDecimal.ROUND_HALF_UP));
        
        pedido.setCodigoRastreio(random.nextBoolean() ? "RASTREIO-" + random.nextInt(1000, 9999) : null);
        pedido.setUrlNf(random.nextBoolean() ? "http://nf.example.com/" + random.nextInt(1000, 9999) : null);
        
        // Initialize empty items list
        pedido.setItens(new ArrayList<>());
        
        return pedido;
    }
}
