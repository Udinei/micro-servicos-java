package com.github.udinei.icompras.pedidos.controller;

import com.github.udinei.icompras.pedidos.dto.NovoPedidoDTO;
import com.github.udinei.icompras.pedidos.dto.PedidoDTO;
import com.github.udinei.icompras.pedidos.mapper.PedidoMapper;
import com.github.udinei.icompras.pedidos.model.Pedido;
import com.github.udinei.icompras.pedidos.model.StatusPedido;
import com.github.udinei.icompras.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;

    /**
     * GET /api/pedidos - Listar todos os pedidos
     */
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarTodos() {
        List<Pedido> pedidos = pedidoService.listarTodos();
        List<PedidoDTO> pedidosDTO = pedidoMapper.map(pedidos);
        return ResponseEntity.ok(pedidosDTO);
    }

    /**
     * GET /api/pedidos/{codigo} - Buscar pedido por código
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<PedidoDTO> buscarPorCodigo(@PathVariable Long codigo) {
        return pedidoService.buscarPorCodigo(codigo)
                .map(pedidoMapper::map)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/pedidos/cliente/{codigoCliente} - Buscar pedidos por cliente
     */
    @GetMapping("/cliente/{codigoCliente}")
    public ResponseEntity<List<PedidoDTO>> buscarPorCliente(@PathVariable Long codigoCliente) {
        List<Pedido> pedidos = pedidoService.buscarPorCliente(codigoCliente);
        List<PedidoDTO> pedidosDTO = pedidoMapper.map(pedidos);
        return ResponseEntity.ok(pedidosDTO);
    }

    /**
     * GET /api/pedidos/status/{status} - Buscar pedidos por status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PedidoDTO>> buscarPorStatus(@PathVariable StatusPedido status) {
        List<Pedido> pedidos = pedidoService.buscarPorStatus(status);
        List<PedidoDTO> pedidosDTO = pedidoMapper.map(pedidos);
        return ResponseEntity.ok(pedidosDTO);
    }

    /**
     * POST /api/pedidos - Criar novo pedido
     */
    @PostMapping
    public ResponseEntity<PedidoDTO> criar(@Valid @RequestBody NovoPedidoDTO novoPedidoDTO) {
        Pedido pedido = pedidoMapper.map(novoPedidoDTO);
        Pedido novoPedido = pedidoService.salvar(pedido);
        PedidoDTO pedidoDTO = pedidoMapper.map(novoPedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDTO);
    }

    /**
     * PUT /api/pedidos/{codigo} - Atualizar pedido existente
     */
    @PutMapping("/{codigo}")
    public ResponseEntity<PedidoDTO> atualizar(
            @PathVariable Long codigo,
            @Valid @RequestBody NovoPedidoDTO novoPedidoDTO) {
        try {
            Pedido pedido = pedidoMapper.map(novoPedidoDTO);
            Pedido pedidoAtualizado = pedidoService.atualizar(codigo, pedido);
            PedidoDTO pedidoDTO = pedidoMapper.map(pedidoAtualizado);
            return ResponseEntity.ok(pedidoDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PATCH /api/pedidos/{codigo}/status - Atualizar apenas o status do pedido
     */
    @PatchMapping("/{codigo}/status")
    public ResponseEntity<PedidoDTO> atualizarStatus(
            @PathVariable Long codigo,
            @RequestParam StatusPedido status) {
        try {
            Pedido pedidoAtualizado = pedidoService.atualizarStatus(codigo, status);
            PedidoDTO pedidoDTO = pedidoMapper.map(pedidoAtualizado);
            return ResponseEntity.ok(pedidoDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/pedidos/{codigo} - Deletar pedido
     */
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletar(@PathVariable Long codigo) {
        if (!pedidoService.existe(codigo)) {
            return ResponseEntity.notFound().build();
        }
        pedidoService.deletar(codigo);
        return ResponseEntity.noContent().build();
    }
}
