package io.github.anderson.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.github.anderson.model.entity.Cidade;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class CidadeRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private CidadeRepository repository;
	
	private Cidade criarCidade() {
		
		return new Cidade("Recife", "Pernambuco");
	}
	
	@Test
	@DisplayName("Deve salvar uma cidade")
	public void salvarTest() {
		
		Cidade cidade = criarCidade();
		
		Cidade cidadeSalva = repository.save(cidade);
		
		assertThat(cidadeSalva.getId()).isNotNull();
		assertThat(cidadeSalva.getNome()).isEqualTo(cidade.getNome());
		assertThat(cidadeSalva.getEstado()).isEqualTo(cidade.getEstado());
	}
	
	@Test
	@DisplayName("Deve retornar verdadeiro se já existir cidade com nome e estado cadastrado")
	public void existeCidadeCadastradaTest() {
		
		Cidade cidade = criarCidade();
		
		entityManager.persist(cidade);
		
		boolean existe = repository.existsByNomeAndEstado("Recife", "Pernambuco");
		
		assertThat(existe).isTrue();
	}
	
	@Test
	@DisplayName("Deve retornar falso se não existir cidade com nome e estado cadastrado")
	public void naoExisteCidadeCadastradaTest() {
		
		boolean existe = repository.existsByNomeAndEstado("Recife", "Pernambuco");
		
		assertThat(existe).isFalse();
	}
	
	@Test
	@DisplayName("Deve encontrar uma cidade pelo Id")
	public void encontrarPorIdTest() {
		
		Cidade cidade = criarCidade();
		
		entityManager.persist(cidade);
		
		Optional<Cidade> cidadeEncontrada = repository.findById(cidade.getId());
		
		assertThat(cidadeEncontrada.isPresent()).isTrue();
	}
}
