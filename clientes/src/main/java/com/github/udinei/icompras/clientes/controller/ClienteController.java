package com.github.udinei.icompras.clientes.controller;

import com.github.udinei.icompras.clientes.model.Cliente;
import com.github.udinei.icompras.clientes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * GET /api/clientes - Listar todos os clientes
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    /**
     * GET /api/clientes/{codigo} - Buscar cliente por código
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Cliente> buscarPorCodigo(@PathVariable Long codigo) {
        return clienteService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/clientes/buscar?nome=xxx - Buscar clientes por nome
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarPorNome(@RequestParam String nome) {
        List<Cliente> clientes = clienteService.buscarPorNome(nome);
        return ResponseEntity.ok(clientes);
    }

    /**
     * POST /api/clientes - Criar novo cliente
     */
    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente) {
        Cliente novoCliente = clienteService.salvar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    /**
     * PUT /api/clientes/{codigo} - Atualizar cliente existente
     */
    @PutMapping("/{codigo}")
    public ResponseEntity<Cliente> atualizar(
            @PathVariable Long codigo,
            @RequestBody Cliente cliente) {
        try {
            Cliente clienteAtualizado = clienteService.atualizar(codigo, cliente);
            return ResponseEntity.ok(clienteAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/clientes/{codigo} - Deletar cliente
     */
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletar(@PathVariable Long codigo) {
        if (!clienteService.existe(codigo)) {
            return ResponseEntity.notFound().build();
        }
        clienteService.deletar(codigo);
        return ResponseEntity.noContent().build();
    }
}
