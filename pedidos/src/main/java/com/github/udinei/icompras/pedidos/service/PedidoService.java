package com.github.udinei.icompras.pedidos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.udinei.icompras.pedidos.dto.DadosPagamentoDTO;
import com.github.udinei.icompras.pedidos.model.*;
import com.github.udinei.icompras.pedidos.model.exception.ItemNaoEncontradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.udinei.icompras.pedidos.client.ServicoBancarioClient;
import com.github.udinei.icompras.pedidos.dto.NovoPedidoDTO;
import com.github.udinei.icompras.pedidos.dto.PedidoDTO;
import com.github.udinei.icompras.pedidos.mapper.PedidoMapper;
import com.github.udinei.icompras.pedidos.repository.ItemPedidoRepository;
import com.github.udinei.icompras.pedidos.repository.PedidoRepository;
import com.github.udinei.icompras.pedidos.validator.PedidoValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final PedidoValidator validator;
    private final ServicoBancarioClient servicoBancarioClient;
    private final ItemPedidoRepository itemPedidoRepository;


    @Transactional
    public Pedido salvar(Pedido pedido) {

        validator.validar(pedido);
        pedido = pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(pedido.getItens());

        var chavePagamento = servicoBancarioClient.solicitarPagamento(pedido);
        pedido.setChavePagamento(chavePagamento);
        return pedido;
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


    public void atualizarStatusPagamento(
            Long codigoPedido, String chavePagamento, boolean sucesso, String observacoes) {
        var pedidoEncontrado = pedidoRepository.findByCodigoAndChavePagamento(codigoPedido, chavePagamento);

        if (pedidoEncontrado.isEmpty()) {
            var msg = String.format("Pedido não encontrado para o código %d e chave pagamento %s",
                    codigoPedido, chavePagamento);
            log.error(msg);
            return;
        }
        Pedido pedido = pedidoEncontrado.get();

        if (sucesso) {
            pedido.setStatus(StatusPedido.PAGO);
        } else {
            pedido.setStatus(StatusPedido.ERRO_PAGAMENTO);
            pedido.setObservacoes(observacoes);
        }

        pedidoRepository.save(pedido);
     }

     @Transactional
     public void adicionarNovoPagamento(
             Long codigoPedido, String dadosCartao, TipoPagamento tipo){

              var pedidoEncontrado = pedidoRepository.findById(codigoPedido);

              if(pedidoEncontrado.isEmpty()){
                  throw new ItemNaoEncontradoException("Pedido não encontrado, para o codigo informado!");
              }

              var pedido = pedidoEncontrado.get();
              DadosPagamento dadosPagamento = new DadosPagamento();
              dadosPagamento.setTipoPagamento(tipo);
              dadosPagamento.setDados(dadosCartao);

              pedido.setDadosPagamento(dadosPagamento);
              pedido.setStatus(StatusPedido.REALIZADO);
              pedido.setObservacoes("Novo pagamento realizado, aguardando processamento.");

              String novaChavepagamento =  servicoBancarioClient.solicitarPagamento(pedido);
              pedido.setChavePagamento(novaChavepagamento);
              pedidoRepository.save(pedido);

     }



}
