package com.github.udinei.icompras.produtos.controller;

import com.github.udinei.icompras.produtos.model.Produto;
import com.github.udinei.icompras.produtos.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    /**
     * GET /api/produtos - Listar todos os produtos
     */
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        List<Produto> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    /**
     * GET /api/produtos/{codigo} - Buscar produto por código
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Produto> buscarPorCodigo(@PathVariable Long codigo) {
        return produtoService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/produtos/buscar?nome=xxx - Buscar produtos por nome
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Produto>> buscarPorNome(@RequestParam String nome) {
        List<Produto> produtos = produtoService.buscarPorNome(nome);
        return ResponseEntity.ok(produtos);
    }

    /**
     * POST /api/produtos - Criar novo produto
     */
    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.salvar(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    /**
     * PUT /api/produtos/{codigo} - Atualizar produto existente
     */
    @PutMapping("/{codigo}")
    public ResponseEntity<Produto> atualizar(
            @PathVariable Long codigo,
            @RequestBody Produto produto) {
        try {
            Produto produtoAtualizado = produtoService.atualizar(codigo, produto);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/produtos/{codigo} - Deletar produto
     */
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletar(@PathVariable Long codigo) {
        if (!produtoService.existe(codigo)) {
            return ResponseEntity.notFound().build();
        }
        produtoService.deletar(codigo);
        return ResponseEntity.noContent().build();
    }
}
