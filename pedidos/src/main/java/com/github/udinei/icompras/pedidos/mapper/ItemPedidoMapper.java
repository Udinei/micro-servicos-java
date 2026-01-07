package com.github.udinei.icompras.pedidos.mapper;

import com.github.udinei.icompras.pedidos.dto.ItemPedidoDTO;
import com.github.udinei.icompras.pedidos.dto.NovoItemPedidoDTO;
import com.github.udinei.icompras.pedidos.model.ItemPedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {
    
    ItemPedido map(ItemPedidoDTO dto);
    ItemPedido map(NovoItemPedidoDTO dto);
    ItemPedidoDTO map(ItemPedido entity);
}
