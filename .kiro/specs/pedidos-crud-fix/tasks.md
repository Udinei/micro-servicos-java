# Implementation Plan - Correção do CRUD de Pedidos

- [x] 1. Corrigir as entidades JPA





  - Substituir `@Data` por `@Getter` e `@Setter` nas entidades Pedido e ItemPedido
  - Adicionar métodos auxiliares para gerenciar relacionamento bidirecional (addItem, removeItem, clearItens)
  - Ajustar configurações de cascade e fetch type
  - _Requirements: 1.2, 1.3, 2.1, 2.3_

- [x] 2. Corrigir o PedidoService





  - Implementar método salvar com associação correta de itens
  - Implementar método atualizar com substituição de itens e orphan removal
  - Adicionar anotações @Transactional apropriadas
  - Implementar tratamento de exceções para entidades não encontradas
  - _Requirements: 2.2, 3.1, 3.2, 4.1, 4.2, 4.3, 4.4, 4.5_

- [x] 2.1 Escrever testes unitários para PedidoService


  - Criar PedidoServiceTest com mocks do repository
  - Testar método salvar com itens
  - Testar método atualizar com substituição de itens
  - Testar métodos de busca (por código, cliente, status)
  - Testar método deletar
  - Testar cenários de erro (entidade não encontrada)
  - _Requirements: 7.2, 7.3, 7.4, 7.5_

- [x] 2.2 Escrever teste de propriedade: Bidirectional relationship establishment


  - **Property 1: Bidirectional relationship establishment**
  - **Validates: Requirements 2.1, 2.3**

- [x] 2.3 Escrever teste de propriedade: Cascade persistence

  - **Property 2: Cascade persistence**
  - **Validates: Requirements 2.2, 3.1, 3.2**

- [x] 2.4 Escrever teste de propriedade: Orphan removal on item removal

  - **Property 3: Orphan removal on item removal**
  - **Validates: Requirements 2.4**


- [ ] 2.5 Escrever teste de propriedade: Default timestamp assignment
  - **Property 4: Default timestamp assignment**
  - **Validates: Requirements 3.4**


- [ ] 2.6 Escrever teste de propriedade: Update replaces items
  - **Property 5: Update replaces items**

  - **Validates: Requirements 4.2, 4.3**

- [ ] 2.7 Escrever teste de propriedade: Orphan removal on update
  - **Property 6: Orphan removal on update**
  - **Validates: Requirements 4.4**

- [ ] 3. Verificar e ajustar o PedidoController
  - Revisar mapeamento de endpoints REST
  - Garantir tratamento adequado de respostas HTTP
  - Adicionar tratamento de exceções com @ExceptionHandler
  - _Requirements: 3.1, 4.5, 6.2, 6.3_

- [ ] 3.1 Escrever testes de integração para PedidoController
  - Criar PedidoControllerTest com MockMvc
  - Testar POST /api/pedidos (criar pedido)
  - Testar GET /api/pedidos (listar todos)
  - Testar GET /api/pedidos/{codigo} (buscar por código)
  - Testar GET /api/pedidos/cliente/{codigoCliente} (filtrar por cliente)
  - Testar GET /api/pedidos/status/{status} (filtrar por status)
  - Testar PUT /api/pedidos/{codigo} (atualizar pedido)
  - Testar PATCH /api/pedidos/{codigo}/status (atualizar status)
  - Testar DELETE /api/pedidos/{codigo} (deletar pedido)
  - Testar cenários de erro (404, 400)
  - _Requirements: 8.2, 8.3, 8.4, 8.5, 8.6_

- [ ] 3.2 Escrever teste de propriedade: Complete retrieval
  - **Property 7: Complete retrieval**
  - **Validates: Requirements 5.1**

- [ ] 3.3 Escrever teste de propriedade: List includes all items
  - **Property 8: List includes all items**
  - **Validates: Requirements 5.2**

- [ ] 3.4 Escrever teste de propriedade: Client filtering accuracy
  - **Property 9: Client filtering accuracy**
  - **Validates: Requirements 5.3**

- [ ] 3.5 Escrever teste de propriedade: Status filtering accuracy
  - **Property 10: Status filtering accuracy**
  - **Validates: Requirements 5.4**

- [ ] 3.6 Escrever teste de propriedade: Cascade deletion
  - **Property 11: Cascade deletion**
  - **Validates: Requirements 6.1**

- [ ] 4. Checkpoint - Garantir que todos os testes passam
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 5. Testar manualmente os endpoints via HTTP client
  - Executar requisições do arquivo api-tests-pedidos.http
  - Verificar criação de pedido com itens
  - Verificar atualização de pedido
  - Verificar consultas e filtros
  - Verificar exclusão de pedido
  - _Requirements: 3.1, 4.1, 5.1, 5.2, 5.3, 5.4, 6.1_
