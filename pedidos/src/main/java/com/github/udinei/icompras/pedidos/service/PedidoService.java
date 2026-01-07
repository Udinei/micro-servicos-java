package com.github.udinei.icompras.pedidos.service;

import com.github.udinei.icompras.pedidos.dto.NovoPedidoDTO;
import com.github.udinei.icompras.pedidos.dto.PedidoDTO;
import com.github.udinei.icompras.pedidos.mapper.PedidoMapper;
import com.github.udinei.icompras.pedidos.model.ItemPedido;
import com.github.udinei.icompras.pedidos.model.Pedido;
import com.github.udinei.icompras.pedidos.model.StatusPedido;
import com.github.udinei.icompras.pedidos.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final Validator validator;

 
    @Transactional
    public Pedido salvar(Pedido pedido) {


        // Associar cada item ao pedido - criar uma cópia da lista para evitar ConcurrentModificationException
        if (pedido.getItens() != null && !pedido.getItens().isEmpty()) {
            List<ItemPedido> itensTemp = new ArrayList<>(pedido.getItens());
            pedido.clearItens();
            itensTemp.forEach(pedido::addItem);
        }
        return pedidoRepository.save(pedido);
    }
    

    @Transactional
    public Pedido atualizar(Long codigo, Pedido pedidoAtualizado) {
        return pedidoRepository.findById(codigo)
                .map(pedido -> {
                    // Atualizar propriedades simples
                    pedido.setCodigoCliente(pedidoAtualizado.getCodigoCliente());
                    pedido.setObservacoes(pedidoAtualizado.getObservacoes());
                    pedido.setStatus(pedidoAtualizado.getStatus());
                    pedido.setTotal(pedidoAtualizado.getTotal());
                    pedido.setCodigoRastreio(pedidoAtualizado.getCodigoRastreio());
                    pedido.setUrlNf(pedidoAtualizado.getUrlNf());
                    
                    // Atualizar dados de pagamento
                    pedido.setTipoPagamento(pedidoAtualizado.getTipoPagamento());
                    pedido.setChavePix(pedidoAtualizado.getChavePix());
                    pedido.setNumeroCartao(pedidoAtualizado.getNumeroCartao());
                    pedido.setCodigoAutorizacao(pedidoAtualizado.getCodigoAutorizacao());
                    pedido.setLinhaDigitavel(pedidoAtualizado.getLinhaDigitavel());
                    pedido.setChavePagamento(pedidoAtualizado.getChavePagamento());
                    
                    // Substituir itens - limpar os antigos e adicionar os novos
                    pedido.clearItens();
                    if (pedidoAtualizado.getItens() != null && !pedidoAtualizado.getItens().isEmpty()) {
                        List<ItemPedido> novosItens = new ArrayList<>(pedidoAtualizado.getItens());
                        novosItens.forEach(pedido::addItem);
                    }
                    
                    return pedidoRepository.save(pedido);
                })
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com código: " + codigo));
    }

    @Transactional
    public Pedido atualizarStatus(Long codigo, StatusPedido novoStatus) {
        return pedidoRepository.findById(codigo)
                .map(pedido -> {
                    pedido.setStatus(novoStatus);
                    return pedidoRepository.save(pedido);
                })
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com código: " + codigo));
    }

    @Transactional
    public void deletar(Long codigo) {
        if (!pedidoRepository.existsById(codigo)) {
            throw new EntityNotFoundException("Pedido não encontrado com código: " + codigo);
        }
        pedidoRepository.deleteById(codigo);
    }

    @Transactional(readOnly = true)
    public boolean existe(Long codigo) {
        return pedidoRepository.existsById(codigo);
    }

    // ========== Métodos que usam DTOs ==========

    @Transactional
    public PedidoDTO criarPedido(NovoPedidoDTO novoPedidoDTO) {
        // O mapper agora faz tudo: converte DTO, mapeia itens, define status, calcula total e estabelece relacionamentos
        Pedido pedido = pedidoMapper.map(novoPedidoDTO);
        
        // Salvar e retornar DTO
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return pedidoMapper.map(pedidoSalvo);
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> listarTodosDTO() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidoMapper.map(pedidos);
    }

    @Transactional(readOnly = true)
    public PedidoDTO buscarPorCodigoDTO(Long codigo) {
        Pedido pedido = pedidoRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com código: " + codigo));
        return pedidoMapper.map(pedido);
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> buscarPorClienteDTO(Long codigoCliente) {
        List<Pedido> pedidos = pedidoRepository.findByCodigoCliente(codigoCliente);
        return pedidoMapper.map(pedidos);
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> buscarPorStatusDTO(StatusPedido status) {
        List<Pedido> pedidos = pedidoRepository.findByStatus(status);
        return pedidoMapper.map(pedidos);
    }

       @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorCodigo(Long codigo) {
        return pedidoRepository.findById(codigo);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorCliente(Long codigoCliente) {
        return pedidoRepository.findByCodigoCliente(codigoCliente);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorStatus(StatusPedido status) {
        return pedidoRepository.findByStatus(status);
    }

    
}
