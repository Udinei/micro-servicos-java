package com.github.udinei.icompras.pedidos.repository;

import com.github.udinei.icompras.pedidos.model.Pedido;
import com.github.udinei.icompras.pedidos.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByCodigoCliente(Long codigoCliente);
    
    List<Pedido> findByStatus(StatusPedido status);
}
