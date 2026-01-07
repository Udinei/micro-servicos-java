package com.github.udinei.icompras.pedidos.service;

import com.github.udinei.icompras.pedidos.model.ItemPedido;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidoListGenerator extends Generator<List<ItemPedido>> {

    @SuppressWarnings("unchecked")
    public ItemPedidoListGenerator() {
        super((Class<List<ItemPedido>>) (Class<?>) List.class);
    }

    @Override
    public List<ItemPedido> generate(SourceOfRandomness random, GenerationStatus status) {
        List<ItemPedido> itens = new ArrayList<>();
        
        // Generate between 0 and 5 items
        int numItems = random.nextInt(0, 5);
        
        for (int i = 0; i < numItems; i++) {
            ItemPedido item = new ItemPedido();
            item.setCodigoProduto(random.nextLong(1, 1000));
            item.setQuantidade(random.nextInt(1, 10));
            item.setValorUnitario(BigDecimal.valueOf(random.nextDouble(1.0, 1000.0))
                                           .setScale(2, BigDecimal.ROUND_HALF_UP));
            itens.add(item);
        }
        
        return itens;
    }
}
