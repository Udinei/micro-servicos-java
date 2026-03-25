package com.github.udinei.icompras.pedidos.repository;

import com.github.udinei.icompras.pedidos.model.Pedido;
import com.github.udinei.icompras.pedidos.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<Pedido> findByCodigoAndChavePagamento(Long codigo, String chavePagameento);

    List<Pedido> findByCodigoCliente(Long codigoCliente);
    
    List<Pedido> findByStatus(StatusPedido status);


}
