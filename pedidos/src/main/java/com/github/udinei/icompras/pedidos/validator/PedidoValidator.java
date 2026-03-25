package com.github.udinei.icompras.pedidos.validator;

import com.github.udinei.icompras.pedidos.model.exception.ValidationException;
import org.springframework.stereotype.Component;

import com.github.udinei.icompras.pedidos.client.ClientesClient;
import com.github.udinei.icompras.pedidos.client.ProdutosClient;
import com.github.udinei.icompras.pedidos.client.representation.ClienteRepresentation;
import com.github.udinei.icompras.pedidos.model.ItemPedido;
import com.github.udinei.icompras.pedidos.model.Pedido;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoValidator {

    private final ProdutosClient produtosClient;
    private final ClientesClient clientesClient;

    public void validar(Pedido pedido) {

        Long codigoCliente = pedido.getCodigoCliente();
        validarCliente(codigoCliente);
        pedido.getItens().forEach(this::validarItem);

        /**
         * if (pedido == null) { throw new IllegalArgumentException("Pedido não
         * pode ser nulo"); } if (pedido.getCodigoCliente() == null) { throw new
         * IllegalArgumentException("Código do cliente é obrigatório"); } if
         * (pedido.getTotal() == null ||
         * pedido.getTotal().compareTo(BigDecimal.ZERO) < 0) { throw new
         * IllegalArgumentException("Total do pedido inválido"); } if
         * (pedido.getItens() == null || pedido.getItens().isEmpty()) { throw
         * new IllegalArgumentException("Pedido deve ter pelo menos um item");
		}
         */
    }

    private void validarCliente(Long codigoCliente) {
        try {
            var response = clientesClient.obterDados(codigoCliente);
            ClienteRepresentation cliente = response.getBody();
            log.info("Cliente de codigo: {} encontrado:{} ", cliente.codigo(), cliente.nome());

        } catch (FeignException.NotFound e) {
            //log.error("Erro ao validar cliente", e);
            var message = String.format("Cliente de código %d não encontrado.", codigoCliente);
            throw new ValidationException("codigoCliente", message);
        }
    }

    private void validarItem(ItemPedido item) {
        try {
            var response = produtosClient.obterDados(item.getCodigoProduto());
            var ProdutoRespresentation = response.getBody();
            log.info("Produto de codigo: {} encontrado:{} ", ProdutoRespresentation.codigo(), ProdutoRespresentation.nome());
        } catch (FeignException.NotFound e) {
            //log.error("Erro ao validar produto", e);
            var message = String.format("Produto de código %d não encontrado.", item.getCodigoProduto());
            throw new ValidationException("codigoProduto", message);

        }
    }

}
