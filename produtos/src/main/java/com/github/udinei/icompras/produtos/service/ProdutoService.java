package com.github.udinei.icompras.produtos.service;

import com.github.udinei.icompras.produtos.model.Produto;
import com.github.udinei.icompras.produtos.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorCodigo(Long codigo) {
        return produtoRepository.findById(codigo);
    }

    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long codigo, Produto produtoAtualizado) {
        return produtoRepository.findById(codigo)
                .map(produto -> {
                    produto.setNome(produtoAtualizado.getNome());
                    produto.setValorUnitario(produtoAtualizado.getValorUnitario());
                    return produtoRepository.save(produto);
                })
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com código: " + codigo));
    }

    public void deletar(Long codigo) {
        produtoRepository.deleteById(codigo);
    }

    public boolean existe(Long codigo) {
        return produtoRepository.existsById(codigo);
    }
}
