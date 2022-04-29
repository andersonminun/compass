package io.github.anderson.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.anderson.model.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	/**
	 * Retorna uma lista de clientes pelo nome
	 */
	public List<Cliente> findByNome(String nome);
}
