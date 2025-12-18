package com.github.udinei.icompras.clientes.repository;

import com.github.udinei.icompras.clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    
    boolean existsByCpf(String cpf);
}
