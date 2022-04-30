package io.github.anderson.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.anderson.api.exception.RegraNegocioException;
import io.github.anderson.model.entity.Cidade;
import io.github.anderson.model.entity.Cliente;
import io.github.anderson.model.repository.CidadeRepository;
import io.github.anderson.model.repository.ClienteRepository;
import io.github.anderson.service.ClienteService;
import io.github.anderson.util.Constantes;

@Service
public class ClienteServiceImpl implements ClienteService {

	private ClienteRepository repository;
	
	private CidadeRepository cidadeRepository;
	
	public ClienteServiceImpl(ClienteRepository repository, CidadeRepository cidadeRepository) {
		super();
		this.repository = repository;
		this.cidadeRepository = cidadeRepository;
	}

	/**
	 * Salva um novo cliente
	 */
	@Override
	public Cliente salvar(Cliente cliente) {
		
		if(!validarSexo(cliente.getSexo())) {
			throw new RegraNegocioException(Constantes.ERROR_SEXO_INVALIDO);
		}
		
		Long idCidade = cliente.getCidade().getId();
		
		Cidade cidade = cidadeRepository.findById(idCidade)
				.orElseThrow(() -> new RegraNegocioException(Constantes.ERROR_CIDADE_INVALIDA));
		
		cliente.setCidade(cidade);
		
		return repository.save(cliente);
	}

	/**
	 * Retorna um cliente pelo Id
	 */
	@Override
	public Optional<Cliente> getPorId(Long id) {
		 
		return repository.findById(id);
	}

	/**
	 * Retorna um cliente pelo Nome
	 */
	@Override
	public List<Cliente> getPorNome(String nome) {
		
		return repository.findByNome(nome);
	}

	/**
	 * Deleta um cliente pelo Id
	 */
	@Override
	public void deletar(Cliente cliente) {
		
		repository.delete(cliente);
	}

	/**
	 * Atualiza o nome do cliente pelo Id
	 */
	@Override
	public Cliente atualizar(Cliente cliente) {
		
		return repository.save(cliente);
	}
	
	/*
	 * Métodos auxiliares
	 */
	
	/**
	 * Valida o valor do sexo informado
	 */
	private boolean validarSexo(String sexo) {
		
		List<String> valoresValidos = 
				Arrays.asList(Constantes.SEXO_MASCULINO, Constantes.SEXO_FEMININO, Constantes.SEXO_NAO_INFORMADO);
		
		return valoresValidos.contains(sexo);
	}

}
