package io.github.anderson.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.anderson.model.entity.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {

	/**
	 * Retorna se já existe salvo uma cidade com o nome e estado informado
	 */
	boolean existsByNomeAndEstado(String nome, String estado);
}
