package io.github.anderson.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.github.anderson.api.exception.RegraNegocioException;
import io.github.anderson.model.entity.Cidade;
import io.github.anderson.model.repository.CidadeRepository;
import io.github.anderson.service.impl.CidadeServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CidadeServiceTest {
	
	private CidadeService service;
	
	@MockBean
	private CidadeRepository repository;

	@BeforeEach
	public void setUp() {
		this.service = new CidadeServiceImpl(repository);
	}
	
	private Cidade criarCidade() {
		
		return new Cidade("Recife", "Pernambuco");
	}
	
	@Test
	@DisplayName("Deve salvar uma cidade")
	public void salvarTest() {
		
		Cidade cidade = criarCidade();
		
		Cidade retorno = criarCidade();
		retorno.setId(1l);
		
		when(repository.existsByNomeAndEstado(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		when(repository.save(cidade)).thenReturn(retorno);
		
		Cidade cidadeSalva = service.salvar(cidade);
		
		assertThat(cidadeSalva.getId()).isEqualTo(retorno.getId());
		assertThat(cidadeSalva.getNome()).isEqualTo(retorno.getNome());
		assertThat(cidadeSalva.getEstado()).isEqualTo(retorno.getEstado());
	}
	
	@Test
	@DisplayName("Deve lançar um erro ao tentar salvar uma cidade com nome e estado já cadastrado")
	public void naoSalvarCidadeDuplicadaTest() {
		
		Cidade cidade = criarCidade();
		
		when(repository.existsByNomeAndEstado(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		
		Throwable exception = Assertions.catchThrowable(() -> service.salvar(cidade));
		
		assertThat(exception)
			.isInstanceOf(RegraNegocioException.class)
			.hasMessage("Cidade já cadastrada");
		
		verify(repository, never()).save(cidade);
	}
	
	@Test
	@DisplayName("Deve retornar uma lista de cidades usando um filtro")
	public void getPorFiltroTest() {
		
		Cidade cidade = criarCidade();
		cidade.setId(1l);
		
		List<Cidade> cidades = Arrays.asList(cidade);
		
		Cidade filtro = new Cidade();
		filtro.setNome("Recife");
		
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.EXACT);

		Example<Cidade> example = Example.of(filtro, matcher);
		
		when(repository.findAll(example)).thenReturn(cidades);
		
		List<Cidade> cidadesRetornada = service.getPorFiltro(filtro);
		
		assertThat(cidadesRetornada).isNotEmpty();
		assertThat(cidadesRetornada.get(0).getId()).isEqualTo(cidade.getId());
		assertThat(cidadesRetornada.get(0).getNome()).isEqualTo(cidade.getNome());
		assertThat(cidadesRetornada.get(0).getEstado()).isEqualTo(cidade.getEstado());
	}
}
