# Requirements Document - Correção do CRUD de Pedidos

## Introduction

Este documento especifica os requisitos para implementar corretamente o CRUD (Create, Read, Update, Delete) do microsserviço de pedidos do sistema iCompras. Este é um novo microsserviço que faz parte da arquitetura de microserviços do iCompras e precisa ter suas funcionalidades básicas implementadas e testadas adequadamente.

## Glossary

- **Sistema de Pedidos**: O microsserviço responsável por gerenciar pedidos de compra no sistema iCompras
- **Pedido**: Entidade que representa uma ordem de compra contendo informações do cliente, itens, status e valores
- **ItemPedido**: Entidade que representa um produto individual dentro de um pedido
- **CRUD**: Operações básicas de Create (Criar), Read (Ler), Update (Atualizar) e Delete (Deletar)
- **JPA**: Java Persistence API, framework para mapeamento objeto-relacional
- **Lombok**: Biblioteca Java que gera código boilerplate automaticamente

## Requirements

### Requirement 1

**User Story:** Como desenvolvedor, quero que as entidades Pedido e ItemPedido tenham getters e setters funcionais, para que o código compile e execute corretamente.

#### Acceptance Criteria

1. WHEN the Sistema de Pedidos compiles THEN the Lombok annotations SHALL generate all required getters and setters
2. WHEN a Pedido is accessed THEN the Sistema de Pedidos SHALL provide methods to get and set all properties
3. WHEN an ItemPedido is accessed THEN the Sistema de Pedidos SHALL provide methods to get and set all properties including the bidirectional relationship with Pedido

### Requirement 2

**User Story:** Como desenvolvedor, quero que o relacionamento bidirecional entre Pedido e ItemPedido funcione corretamente, para que os itens sejam persistidos junto com o pedido.

#### Acceptance Criteria

1. WHEN a Pedido is created with ItemPedido instances THEN the Sistema de Pedidos SHALL establish the bidirectional relationship automatically
2. WHEN a Pedido is saved THEN the Sistema de Pedidos SHALL persist all associated ItemPedido instances in cascade
3. WHEN an ItemPedido is added to a Pedido THEN the Sistema de Pedidos SHALL set the parent Pedido reference in the ItemPedido
4. WHEN an ItemPedido is removed from a Pedido THEN the Sistema de Pedidos SHALL remove the ItemPedido from the database due to orphan removal

### Requirement 3

**User Story:** Como desenvolvedor, quero que a operação de criação de pedido funcione corretamente, para que novos pedidos com itens sejam salvos no banco de dados.

#### Acceptance Criteria

1. WHEN a new Pedido is submitted via POST endpoint THEN the Sistema de Pedidos SHALL save the Pedido with all ItemPedido instances
2. WHEN a Pedido is saved THEN the Sistema de Pedidos SHALL return the saved entity with generated IDs
3. WHEN a Pedido is created without itens THEN the Sistema de Pedidos SHALL save the Pedido with an empty items list
4. WHEN a Pedido is created THEN the Sistema de Pedidos SHALL set the dataPedido to current timestamp if not provided

### Requirement 4

**User Story:** Como desenvolvedor, quero que a operação de atualização de pedido funcione corretamente, para que pedidos existentes possam ser modificados incluindo seus itens.

#### Acceptance Criteria

1. WHEN a Pedido is updated via PUT endpoint THEN the Sistema de Pedidos SHALL update all Pedido properties
2. WHEN a Pedido is updated with new ItemPedido instances THEN the Sistema de Pedidos SHALL replace the old items with the new ones
3. WHEN a Pedido is updated THEN the Sistema de Pedidos SHALL maintain the bidirectional relationship between Pedido and ItemPedido
4. WHEN a Pedido update removes ItemPedido instances THEN the Sistema de Pedidos SHALL delete the orphaned items from the database
5. WHEN an update is attempted on a non-existent Pedido THEN the Sistema de Pedidos SHALL return a 404 Not Found response

### Requirement 5

**User Story:** Como desenvolvedor, quero que as operações de leitura de pedido funcionem corretamente, para que pedidos possam ser consultados com todos seus itens.

#### Acceptance Criteria

1. WHEN a Pedido is retrieved by codigo THEN the Sistema de Pedidos SHALL return the Pedido with all associated ItemPedido instances
2. WHEN all Pedidos are listed THEN the Sistema de Pedidos SHALL return all Pedidos with their ItemPedido instances
3. WHEN Pedidos are filtered by codigoCliente THEN the Sistema de Pedidos SHALL return only Pedidos belonging to that client
4. WHEN Pedidos are filtered by StatusPedido THEN the Sistema de Pedidos SHALL return only Pedidos with that status

### Requirement 6

**User Story:** Como desenvolvedor, quero que a operação de exclusão de pedido funcione corretamente, para que pedidos possam ser removidos do sistema.

#### Acceptance Criteria

1. WHEN a Pedido is deleted THEN the Sistema de Pedidos SHALL remove the Pedido and all associated ItemPedido instances
2. WHEN a delete is attempted on a non-existent Pedido THEN the Sistema de Pedidos SHALL return a 404 Not Found response
3. WHEN a Pedido is deleted THEN the Sistema de Pedidos SHALL return a 204 No Content response

### Requirement 7

**User Story:** Como desenvolvedor, quero testes unitários para o PedidoService, para garantir que a lógica de negócio funciona corretamente.

#### Acceptance Criteria

1. WHEN the test suite runs THEN the Sistema de Pedidos SHALL execute unit tests for all PedidoService methods
2. WHEN testing salvar method THEN the Sistema de Pedidos SHALL verify that items are correctly associated with the pedido
3. WHEN testing atualizar method THEN the Sistema de Pedidos SHALL verify that existing pedidos are updated correctly
4. WHEN testing buscar methods THEN the Sistema de Pedidos SHALL verify that queries return correct results
5. WHEN testing deletar method THEN the Sistema de Pedidos SHALL verify that pedidos are removed correctly

### Requirement 8

**User Story:** Como desenvolvedor, quero testes de integração para o PedidoController, para garantir que os endpoints REST funcionam corretamente.

#### Acceptance Criteria

1. WHEN the test suite runs THEN the Sistema de Pedidos SHALL execute integration tests for all REST endpoints
2. WHEN testing POST endpoint THEN the Sistema de Pedidos SHALL verify that new pedidos are created with status 201
3. WHEN testing GET endpoints THEN the Sistema de Pedidos SHALL verify that pedidos are retrieved correctly
4. WHEN testing PUT endpoint THEN the Sistema de Pedidos SHALL verify that pedidos are updated correctly
5. WHEN testing DELETE endpoint THEN the Sistema de Pedidos SHALL verify that pedidos are deleted with status 204
6. WHEN testing error scenarios THEN the Sistema de Pedidos SHALL verify that appropriate HTTP status codes are returned
