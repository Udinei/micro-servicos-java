package com.github.udinei.icompras.pedidos.repository;

import java.util.List;

import com.github.udinei.icompras.pedidos.model.ItemPedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.udinei.icompras.pedidos.model.Pedido;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

 List<ItemPedido> findByPedido(Pedido pedido);

}
