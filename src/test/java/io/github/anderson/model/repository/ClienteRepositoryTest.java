package io.github.anderson.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
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
import io.github.anderson.model.entity.Cliente;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class ClienteRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private ClienteRepository repository;
	
	private Cidade criarCidade() {
		
		return new Cidade("Recife", "Pernambuco");
	}
	
	private Cliente criarCliente(Cidade cidade) {
		
		return new Cliente("Anderson", "M", LocalDate.of(1989, 1, 19), cidade);
	}
	
	@Test
	@DisplayName("Deve salvar uma cidade")
	public void salvarTest() {
		
		Cidade cidade = criarCidade();
		
		entityManager.persist(cidade);
		
		Cliente cliente = criarCliente(cidade);
		
		Cliente clienteSalvo = repository.save(cliente);
		
		assertThat(clienteSalvo.getId()).isNotNull();
		assertThat(clienteSalvo.getNome()).isEqualTo(cliente.getNome());
		assertThat(clienteSalvo.getSexo()).isEqualTo(cliente.getSexo());
		assertThat(clienteSalvo.getDataNascimento()).isEqualTo(cliente.getDataNascimento());
		assertThat(clienteSalvo.getIdade()).isEqualTo(33);
		assertThat(clienteSalvo.getCidade().getId()).isEqualTo(cidade.getId());
	}
	
	@Test
	@DisplayName("Deve encontrar os clientes por nome")
	public void encontrarPorNome() {
		
		Cidade cidade = criarCidade();
		
		entityManager.persist(cidade);
		
		Cliente cliente = criarCliente(cidade);
		
		entityManager.persist(cliente);
		
		List<Cliente> clientes = repository.findByNome("Anderson");
		
		assertThat(clientes).isNotEmpty();
	}
	
	@Test
	@DisplayName("Deve encontrar um cliente pelo Id")
	public void encontrarPorIdTest() {
		
		Cidade cidade = criarCidade();
		
		entityManager.persist(cidade);
		
		Cliente cliente = criarCliente(cidade);
		
		entityManager.persist(cliente);
		
		Optional<Cliente> clienteEncontrado = repository.findById(cliente.getId());
		
		assertThat(clienteEncontrado.isPresent()).isTrue();
	}
	
	@Test
	@DisplayName("Deve deletar um cliente")
	public void deletarTest() {
		
		Cidade cidade = criarCidade();
		
		entityManager.persist(cidade);
		
		Cliente cliente = criarCliente(cidade);
		
		entityManager.persist(cliente);
		
		Cliente clienteEncontrado = entityManager.find(Cliente.class, cliente.getId());
		
		repository.delete(clienteEncontrado);
		
		Cliente clienteDeletado = entityManager.find(Cliente.class, cliente.getId());
		
		assertThat(clienteDeletado).isNull();
	}
}
