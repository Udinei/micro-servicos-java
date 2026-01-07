package com.github.udinei.icompras.pedidos.service;

import com.github.udinei.icompras.pedidos.model.ItemPedido;
import com.github.udinei.icompras.pedidos.model.Pedido;
import com.github.udinei.icompras.pedidos.model.StatusPedido;
import com.github.udinei.icompras.pedidos.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;
    private ItemPedido item1;
    private ItemPedido item2;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setCodigo(1L);
        pedido.setCodigoCliente(100L);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setTotal(BigDecimal.valueOf(150.00));

        item1 = new ItemPedido();
        item1.setCodigo(1L);
        item1.setCodigoProduto(10L);
        item1.setQuantidade(2);
        item1.setValorUnitario(BigDecimal.valueOf(50.00));

        item2 = new ItemPedido();
        item2.setCodigo(2L);
        item2.setCodigoProduto(20L);
        item2.setQuantidade(1);
        item2.setValorUnitario(BigDecimal.valueOf(50.00));
    }

    @Test
    void testSalvarPedidoComItens() {
        // Arrange
        List<ItemPedido> itens = new ArrayList<>(Arrays.asList(item1, item2));
        pedido.setItens(itens);
        
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Act
        Pedido resultado = pedidoService.salvar(pedido);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getItens().size());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        
        // Verify bidirectional relationship
        resultado.getItens().forEach(item -> 
            assertEquals(pedido, item.getPedido())
        );
    }

    @Test
    void testSalvarPedidoSemItens() {
        // Arrange
        pedido.setItens(new ArrayList<>());
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Act
        Pedido resultado = pedidoService.salvar(pedido);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.getItens().isEmpty());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testAtualizarPedidoComSubstituicaoDeItens() {
        // Arrange
        List<ItemPedido> itensAntigos = new ArrayList<>(Arrays.asList(item1));
        pedido.setItens(itensAntigos);
        
        ItemPedido novoItem = new ItemPedido();
        novoItem.setCodigo(3L);
        novoItem.setCodigoProduto(30L);
        novoItem.setQuantidade(3);
        novoItem.setValorUnitario(BigDecimal.valueOf(30.00));
        
        Pedido pedidoAtualizado = new Pedido();
        pedidoAtualizado.setCodigoCliente(100L);
        pedidoAtualizado.setStatus(StatusPedido.PAGO);
        pedidoAtualizado.setTotal(BigDecimal.valueOf(90.00));
        pedidoAtualizado.setItens(new ArrayList<>(Arrays.asList(novoItem)));
        
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Act
        Pedido resultado = pedidoService.atualizar(1L, pedidoAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(StatusPedido.PAGO, resultado.getStatus());
        assertEquals(1, resultado.getItens().size());
        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testAtualizarPedidoNaoEncontrado() {
        // Arrange
        when(pedidoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            pedidoService.atualizar(999L, pedido)
        );
        verify(pedidoRepository, times(1)).findById(999L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testBuscarPorCodigo() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Act
        Optional<Pedido> resultado = pedidoService.buscarPorCodigo(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getCodigo());
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorCodigoNaoEncontrado() {
        // Arrange
        when(pedidoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Pedido> resultado = pedidoService.buscarPorCodigo(999L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(pedidoRepository, times(1)).findById(999L);
    }

    @Test
    void testBuscarPorCliente() {
        // Arrange
        List<Pedido> pedidos = Arrays.asList(pedido);
        when(pedidoRepository.findByCodigoCliente(100L)).thenReturn(pedidos);

        // Act
        List<Pedido> resultado = pedidoService.buscarPorCliente(100L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(100L, resultado.get(0).getCodigoCliente());
        verify(pedidoRepository, times(1)).findByCodigoCliente(100L);
    }

    @Test
    void testBuscarPorStatus() {
        // Arrange
        List<Pedido> pedidos = Arrays.asList(pedido);
        when(pedidoRepository.findByStatus(StatusPedido.REALIZADO)).thenReturn(pedidos);

        // Act
        List<Pedido> resultado = pedidoService.buscarPorStatus(StatusPedido.REALIZADO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(StatusPedido.REALIZADO, resultado.get(0).getStatus());
        verify(pedidoRepository, times(1)).findByStatus(StatusPedido.REALIZADO);
    }

    @Test
    void testListarTodos() {
        // Arrange
        List<Pedido> pedidos = Arrays.asList(pedido);
        when(pedidoRepository.findAll()).thenReturn(pedidos);

        // Act
        List<Pedido> resultado = pedidoService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void testDeletar() {
        // Arrange
        when(pedidoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pedidoRepository).deleteById(1L);

        // Act
        pedidoService.deletar(1L);

        // Assert
        verify(pedidoRepository, times(1)).existsById(1L);
        verify(pedidoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletarPedidoNaoEncontrado() {
        // Arrange
        when(pedidoRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            pedidoService.deletar(999L)
        );
        verify(pedidoRepository, times(1)).existsById(999L);
        verify(pedidoRepository, never()).deleteById(anyLong());
    }

    @Test
    void testAtualizarStatus() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Act
        Pedido resultado = pedidoService.atualizarStatus(1L, StatusPedido.ENVIADO);

        // Assert
        assertNotNull(resultado);
        assertEquals(StatusPedido.ENVIADO, resultado.getStatus());
        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testAtualizarStatusPedidoNaoEncontrado() {
        // Arrange
        when(pedidoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            pedidoService.atualizarStatus(999L, StatusPedido.ENVIADO)
        );
        verify(pedidoRepository, times(1)).findById(999L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testExiste() {
        // Arrange
        when(pedidoRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean resultado = pedidoService.existe(1L);

        // Assert
        assertTrue(resultado);
        verify(pedidoRepository, times(1)).existsById(1L);
    }

    @Test
    void testNaoExiste() {
        // Arrange
        when(pedidoRepository.existsById(anyLong())).thenReturn(false);

        // Act
        boolean resultado = pedidoService.existe(999L);

        // Assert
        assertFalse(resultado);
        verify(pedidoRepository, times(1)).existsById(999L);
    }
}
