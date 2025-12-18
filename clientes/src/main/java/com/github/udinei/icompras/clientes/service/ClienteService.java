package com.github.udinei.icompras.clientes.service;

import com.github.udinei.icompras.clientes.model.Cliente;
import com.github.udinei.icompras.clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorCodigo(Long codigo) {
        return clienteRepository.findById(codigo);
    }

    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Long codigo, Cliente clienteAtualizado) {
        return clienteRepository.findById(codigo)
                .map(cliente -> {
                    cliente.setNome(clienteAtualizado.getNome());
                    cliente.setCpf(clienteAtualizado.getCpf());
                    cliente.setLogradouro(clienteAtualizado.getLogradouro());
                    cliente.setNumero(clienteAtualizado.getNumero());
                    cliente.setBairro(clienteAtualizado.getBairro());
                    cliente.setEmail(clienteAtualizado.getEmail());
                    cliente.setTelefone(clienteAtualizado.getTelefone());
                    return clienteRepository.save(cliente);
                })
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com código: " + codigo));
    }

    public void deletar(Long codigo) {
        clienteRepository.deleteById(codigo);
    }

    public boolean existe(Long codigo) {
        return clienteRepository.existsById(codigo);
    }

    public boolean existeCpf(String cpf) {
        return clienteRepository.existsByCpf(cpf);
    }
}
