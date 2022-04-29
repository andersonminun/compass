package io.github.anderson.service;

import java.util.List;
import java.util.Optional;

import io.github.anderson.model.entity.Cliente;

public interface ClienteService {

	/**
	 * Salva um novo cliente
	 */
	public Cliente salvar(Cliente cliente);
	
	/**
	 * Retorna um cliente pelo Id
	 */
	public Optional<Cliente> getPorId(Long id);
	
	/**
	 * Retorna um cliente pelo Nome
	 */
	public List<Cliente> getPorNome(String nome);
	
	/**
	 * Deleta um cliente pelo Id
	 */
	public void deletar(Cliente cliente);
	
	/**
	 * Atualiza o nome do cliente pelo Id
	 */
	public Cliente atualizar(Cliente cliente);
}
