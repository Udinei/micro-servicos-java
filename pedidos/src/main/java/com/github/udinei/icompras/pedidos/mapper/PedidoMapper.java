package com.github.udinei.icompras.pedidos.mapper;

import com.github.udinei.icompras.pedidos.dto.DadosPagamentoDTO;
import com.github.udinei.icompras.pedidos.dto.NovoItemPedidoDTO;
import com.github.udinei.icompras.pedidos.dto.NovoPedidoDTO;
import com.github.udinei.icompras.pedidos.dto.PedidoDTO;
import com.github.udinei.icompras.pedidos.model.ItemPedido;
import com.github.udinei.icompras.pedidos.model.Pedido;
import com.github.udinei.icompras.pedidos.model.StatusPedido;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PedidoMapper {
    
    ItemPedidoMapper ITEM_PEDIDO_MAPPER = Mappers.getMapper(ItemPedidoMapper.class);
    
    @Mapping(source = "itens", target = "itens", qualifiedByName = "mapItens")
    @Mapping(target = "dadosPagamento", ignore = true)
    @Mapping(source = "dadosPagamento.tipoPagamento", target = "tipoPagamento")
    @Mapping(source = "dadosPagamento.chavePix", target = "chavePix")
    @Mapping(source = "dadosPagamento.numeroCartao", target = "numeroCartao")
    @Mapping(source = "dadosPagamento.codigoAutorizacao", target = "codigoAutorizacao")
    @Mapping(source = "dadosPagamento.linhaDigitavel", target = "linhaDigitavel")
    Pedido map(NovoPedidoDTO dto);
    
    @Named("mapItens")
    default List<ItemPedido> mapItens(List<NovoItemPedidoDTO> dtos) {
        return dtos.stream().map(ITEM_PEDIDO_MAPPER::map).collect(Collectors.toList());
    }
    
    @AfterMapping
    default void afterMapping(@MappingTarget Pedido pedido) {
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setDataPedido(LocalDateTime.now());
        var total = calcularTotal(pedido);
        pedido.setTotal(total);
        pedido.getItens().forEach(item -> item.setPedido(pedido));
    }
    
    private static BigDecimal calcularTotal(Pedido pedido) {
        return pedido.getItens().stream()
                .map(item -> item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .abs();
    }
    
    default PedidoDTO map(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        
        // Criar o objeto DadosPagamentoDTO a partir dos campos individuais da entidade
        DadosPagamentoDTO dadosPagamento = null;
        if (pedido.getTipoPagamento() != null) {
            dadosPagamento = new DadosPagamentoDTO(
                pedido.getTipoPagamento(),
                pedido.getChavePix(),
                pedido.getNumeroCartao(),
                pedido.getCodigoAutorizacao(),
                pedido.getLinhaDigitavel()
            );
        }
        
        return new PedidoDTO(
            pedido.getCodigo(),
            pedido.getCodigoCliente(),
            pedido.getDataPedido(),
            dadosPagamento,
            pedido.getObservacoes(),
            pedido.getStatus(),
            pedido.getTotal(),
            pedido.getCodigoRastreio(),
            pedido.getUrlNf(),
            pedido.getChavePagamento(),
            pedido.getItens().stream()
                .map(item -> ITEM_PEDIDO_MAPPER.map(item))
                .collect(Collectors.toList())
        );
    }
    
    List<PedidoDTO> map(List<Pedido> pedidos);
}
