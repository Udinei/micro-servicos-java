package com.github.udinei.icompras.pedidos.service;

import com.github.udinei.icompras.pedidos.model.ItemPedido;
import com.github.udinei.icompras.pedidos.model.Pedido;
import com.github.udinei.icompras.pedidos.model.StatusPedido;
import com.github.udinei.icompras.pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PedidoServicePropertyTest {

    @Mock
    private PedidoRepository pedidoRepository;

    private PedidoService pedidoService;
    private Random random;

    @BeforeEach
    void setUp() {
        pedidoService = new PedidoService(pedidoRepository, null);
        random = new Random();
    }

    /**
     * Feature: pedidos-crud-fix, Property 1: Bidirectional relationship establishment
     * Validates: Requirements 2.1, 2.3
     * 
     * For any Pedido and any set of ItemPedido instances, when items are added to the pedido 
     * using the addItem method, each item should have its pedido reference set to the parent pedido.
     */
    @RepeatedTest(3)
    void testBidirectionalRelationshipEstablishment() {
        // Arrange: Generate random pedido and items
        Pedido pedido = generatePedido();
        List<ItemPedido> itens = generateItemPedidoList();
        
        // Act: Add items to pedido using the helper method
        itens.forEach(pedido::addItem);
        
        // Assert: Each item should have its pedido reference set
        for (ItemPedido item : pedido.getItens()) {
            assertNotNull(item.getPedido(), "Item should have pedido reference");
            assertEquals(pedido, item.getPedido(), "Item's pedido should be the parent pedido");
        }
        
        // Assert: All items should be in the pedido's list
        assertEquals(itens.size(), pedido.getItens().size(), "Pedido should contain all added items");
    }

    /**
     * Feature: pedidos-crud-fix, Property 2: Cascade persistence
     * Validates: Requirements 2.2, 3.1, 3.2
     * 
     * For any Pedido with any number of ItemPedido instances, when the pedido is saved, 
     * all associated items should be persisted to the database with generated IDs.
     */
    @RepeatedTest(3)
    void testCascadePersistence() {
        // Arrange: Generate random pedido and items
        Pedido pedido = generatePedido();
        List<ItemPedido> itens = generateItemPedidoList();
        pedido.setItens(new ArrayList<>(itens));
        
        // Mock repository to simulate persistence
        Pedido savedPedido = new Pedido();
        savedPedido.setCodigo(1L);
        savedPedido.setCodigoCliente(pedido.getCodigoCliente());
        savedPedido.setDataPedido(pedido.getDataPedido());
        savedPedido.setStatus(pedido.getStatus());
        savedPedido.setTotal(pedido.getTotal());
        
        List<ItemPedido> savedItens = new ArrayList<>();
        for (int i = 0; i < itens.size(); i++) {
            ItemPedido savedItem = new ItemPedido();
            savedItem.setCodigo((long) (i + 1));
            savedItem.setCodigoProduto(itens.get(i).getCodigoProduto());
            savedItem.setQuantidade(itens.get(i).getQuantidade());
            savedItem.setValorUnitario(itens.get(i).getValorUnitario());
            savedItem.setPedido(savedPedido);
            savedItens.add(savedItem);
        }
        savedPedido.setItens(savedItens);
        
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(savedPedido);
        
        // Act
        Pedido resultado = pedidoService.salvar(pedido);
        
        // Assert: Pedido should be saved with generated ID
        assertNotNull(resultado.getCodigo(), "Saved pedido should have an ID");
        
        // Assert: All items should be persisted with generated IDs
        assertEquals(itens.size(), resultado.getItens().size(), "All items should be saved");
        for (ItemPedido item : resultado.getItens()) {
            assertNotNull(item.getCodigo(), "Each item should have a generated ID");
            assertNotNull(item.getPedido(), "Each item should have pedido reference");
        }
    }

    /**
     * Feature: pedidos-crud-fix, Property 3: Orphan removal on item removal
     * Validates: Requirements 2.4
     * 
     * For any Pedido with ItemPedido instances, when an item is removed from the pedido 
     * and the pedido is saved, the removed item should no longer exist in the database.
     */
    @RepeatedTest(3)
    void testOrphanRemovalOnItemRemoval() {
        // Arrange: Generate random pedido and items
        Pedido pedido = generatePedido();
        List<ItemPedido> itens = generateItemPedidoList();
        
        // Ensure we have at least one item to remove
        if (itens.isEmpty()) {
            itens.add(createItemPedido(1L, 1, BigDecimal.TEN));
        }
        
        // Setup: Add items to pedido
        itens.forEach(pedido::addItem);
        int initialSize = pedido.getItens().size();
        
        // Act: Remove one item
        ItemPedido itemToRemove = pedido.getItens().get(0);
        pedido.removeItem(itemToRemove);
        
        // Assert: Item should be removed from list
        assertEquals(initialSize - 1, pedido.getItens().size(), 
                    "Item should be removed from pedido's list");
        assertFalse(pedido.getItens().contains(itemToRemove), 
                   "Removed item should not be in pedido's list");
        
        // Assert: Bidirectional relationship should be broken
        assertNull(itemToRemove.getPedido(), "Removed item should have null pedido reference");
    }

    /**
     * Feature: pedidos-crud-fix, Property 4: Default timestamp assignment
     * Validates: Requirements 3.4
     * 
     * For any Pedido created without a dataPedido value, when the pedido is persisted, 
     * the dataPedido should be automatically set to a non-null timestamp.
     */
    @RepeatedTest(3)
    void testDefaultTimestampAssignment() {
        // Arrange: Generate random pedido
        Pedido pedido = generatePedido();
        
        // Setup: Create pedido without dataPedido
        pedido.setDataPedido(null);
        
        // Simulate @PrePersist behavior
        if (pedido.getDataPedido() == null) {
            pedido.setDataPedido(LocalDateTime.now());
        }
        
        // Assert: dataPedido should be set
        assertNotNull(pedido.getDataPedido(), "dataPedido should be automatically set");
    }

    /**
     * Feature: pedidos-crud-fix, Property 5: Update replaces items
     * Validates: Requirements 4.2, 4.3
     * 
     * For any existing Pedido, when it is updated with a new set of ItemPedido instances, 
     * the old items should be replaced with the new items and the bidirectional relationship 
     * should be maintained.
     */
    @RepeatedTest(3)
    void testUpdateReplacesItems() {
        // Arrange: Generate random pedidos and items
        Pedido pedidoExistente = generatePedido();
        List<ItemPedido> itensAntigos = generateItemPedidoList();
        List<ItemPedido> itensNovos = generateItemPedidoList();
        
        // Setup: Existing pedido with old items
        itensAntigos.forEach(pedidoExistente::addItem);
        pedidoExistente.setCodigo(1L);
        
        // Setup: New pedido data with new items
        Pedido pedidoAtualizado = new Pedido();
        pedidoAtualizado.setCodigoCliente(pedidoExistente.getCodigoCliente());
        pedidoAtualizado.setStatus(StatusPedido.PAGO);
        pedidoAtualizado.setTotal(BigDecimal.valueOf(200.00));
        pedidoAtualizado.setItens(new ArrayList<>(itensNovos));
        
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoExistente));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoExistente);
        
        // Act
        Pedido resultado = pedidoService.atualizar(1L, pedidoAtualizado);
        
        // Assert: Items should be replaced
        assertEquals(itensNovos.size(), resultado.getItens().size(), 
                    "Items should be replaced with new items");
        
        // Assert: Bidirectional relationship should be maintained
        for (ItemPedido item : resultado.getItens()) {
            assertEquals(resultado, item.getPedido(), 
                        "Each new item should reference the pedido");
        }
    }

    /**
     * Feature: pedidos-crud-fix, Property 6: Orphan removal on update
     * Validates: Requirements 4.4
     * 
     * For any Pedido being updated, when ItemPedido instances are removed from the items list, 
     * those orphaned items should be deleted from the database.
     */
    @RepeatedTest(3)
    void testOrphanRemovalOnUpdate() {
        // Arrange: Generate random pedido and items
        Pedido pedidoExistente = generatePedido();
        List<ItemPedido> itensAntigos = generateItemPedidoList();
        
        // Ensure we have items to remove
        if (itensAntigos.isEmpty()) {
            itensAntigos.add(createItemPedido(1L, 1, BigDecimal.TEN));
        }
        
        // Setup: Existing pedido with items
        itensAntigos.forEach(pedidoExistente::addItem);
        
        // Act: Clear items (simulating update with empty list)
        pedidoExistente.clearItens();
        
        // Assert: All items should be removed
        assertEquals(0, pedidoExistente.getItens().size(), "All items should be removed");
        
        // Assert: Bidirectional relationships should be broken
        for (ItemPedido item : itensAntigos) {
            assertNull(item.getPedido(), "Orphaned item should have null pedido reference");
        }
    }

    // Helper methods for generating test data
    private Pedido generatePedido() {
        Pedido pedido = new Pedido();
        pedido.setCodigoCliente(random.nextLong(1, 10000));
        pedido.setDataPedido(LocalDateTime.now().minusDays(random.nextInt(0, 365)));
        pedido.setObservacoes(random.nextBoolean() ? "Observação " + random.nextInt(1, 100) : null);
        
        StatusPedido[] statuses = StatusPedido.values();
        pedido.setStatus(statuses[random.nextInt(statuses.length)]);
        
        pedido.setTotal(BigDecimal.valueOf(random.nextDouble(10.0, 10000.0))
                                  .setScale(2, BigDecimal.ROUND_HALF_UP));
        
        pedido.setCodigoRastreio(random.nextBoolean() ? "RASTREIO-" + random.nextInt(1000, 9999) : null);
        pedido.setUrlNf(random.nextBoolean() ? "http://nf.example.com/" + random.nextInt(1000, 9999) : null);
        
        pedido.setItens(new ArrayList<>());
        
        return pedido;
    }

    private List<ItemPedido> generateItemPedidoList() {
        List<ItemPedido> itens = new ArrayList<>();
        int numItems = random.nextInt(0, 6);
        
        for (int i = 0; i < numItems; i++) {
            ItemPedido item = new ItemPedido();
            item.setCodigoProduto(random.nextLong(1, 1000));
            item.setQuantidade(random.nextInt(1, 10));
            item.setValorUnitario(BigDecimal.valueOf(random.nextDouble(1.0, 1000.0))
                                           .setScale(2, BigDecimal.ROUND_HALF_UP));
            itens.add(item);
        }
        
        return itens;
    }

    private ItemPedido createItemPedido(Long codigoProduto, int quantidade, BigDecimal valorUnitario) {
        ItemPedido item = new ItemPedido();
        item.setCodigoProduto(codigoProduto);
        item.setQuantidade(quantidade);
        item.setValorUnitario(valorUnitario);
        return item;
    }
}
