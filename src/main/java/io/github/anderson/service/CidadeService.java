package io.github.anderson.service;

import java.util.List;

import io.github.anderson.model.entity.Cidade;

public interface CidadeService {

	/**
	 * Salva uma nova cidade
	 */
	public Cidade salvar(Cidade cidade);
	
	/**
	 * Retorna uma lista de cidades por filtro
	 */
	public List<Cidade> getPorFiltro(Cidade filtro);

}
