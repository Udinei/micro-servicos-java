
package com.github.udinei.icompras.pedidos.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.github.udinei.icompras.pedidos.client.ClientesClient;
import com.github.udinei.icompras.pedidos.client.ProdutosClient;
import com.github.udinei.icompras.pedidos.model.ItemPedido;
import com.github.udinei.icompras.pedidos.model.Pedido;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class PedidoValidator {

    private final ProdutosClient produtosClient;
	private final ClientesClient clientesClient;


	public void validar(Pedido pedido) {

		Long codigoCliente = pedido.getCodigoCliente();
		validarCliente(codigoCliente);
		pedido.getItens().forEach(this::validarItem);


		/**if (pedido == null) {
			throw new IllegalArgumentException("Pedido não pode ser nulo");
		}
		if (pedido.getCodigoCliente() == null) {
			throw new IllegalArgumentException("Código do cliente é obrigatório");
		}
		if (pedido.getTotal() == null || pedido.getTotal().compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Total do pedido inválido");
		}
		if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
			throw new IllegalArgumentException("Pedido deve ter pelo menos um item");
		}*/
	}

	private void validarCliente(Long codigoCliente) {
		
	}

	private void validarItem(ItemPedido item) {
		
    }
