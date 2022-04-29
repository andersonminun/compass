package io.github.anderson.service.impl;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import io.github.anderson.api.exception.RegraNegocioException;
import io.github.anderson.model.entity.Cidade;
import io.github.anderson.model.repository.CidadeRepository;
import io.github.anderson.service.CidadeService;

@Service
public class CidadeServiceImpl implements CidadeService {

	private CidadeRepository repository;
	
	public CidadeServiceImpl(CidadeRepository repository) {
		this.repository = repository;
	}

	/**
	 * Salva uma nova cidade
	 */
	@Override
	public Cidade salvar(Cidade cidade) {

		if(repository.existsByNomeAndEstado(cidade.getNome(), cidade.getEstado())) {
			throw new RegraNegocioException("Cidade já cadastrada");
		}
		
		return repository.save(cidade);
	}

	/**
	 * Retorna uma lista de cidades por filtro
	 */
	@Override
	public List<Cidade> getPorFiltro(Cidade filtro) {
		
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.EXACT);

		Example<Cidade> example = Example.of(filtro, matcher);
		
		return repository.findAll(example);
	}

}
