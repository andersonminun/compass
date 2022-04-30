package io.github.anderson.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.github.anderson.api.exception.RegraNegocioException;
import io.github.anderson.model.entity.Cidade;
import io.github.anderson.model.entity.Cliente;
import io.github.anderson.model.repository.CidadeRepository;
import io.github.anderson.model.repository.ClienteRepository;
import io.github.anderson.service.impl.ClienteServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ClienteServiceTest {

	private ClienteService service;
	
	@MockBean
	private ClienteRepository repository;
	
	@MockBean
	private CidadeRepository cidadeRepository;
	
	@BeforeEach
	public void setUp() {
		this.service = new ClienteServiceImpl(repository, cidadeRepository);
	}
	
	private Cidade criarCidade() {
		
		Cidade cidade = new Cidade("Recife", "Pernambuco");
		cidade.setId(1l);
		
		return cidade;
	}
	
	private Cliente criarCliente() {
		
		Cidade cidade = criarCidade();
		
		return new Cliente("Anderson", "M", LocalDate.of(1989, 1, 19), cidade);
	}
	
	@Test
	@DisplayName("Deve salvar um cliente")
	public void salvarTest() {
		
		Cidade cidade = criarCidade();
		Cliente cliente = criarCliente();
		
		Cliente retorno = criarCliente();
		retorno.setId(1l);
		
		when(cidadeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(cidade));
		when(repository.save(cliente)).thenReturn(retorno);
		
		Cliente clienteSalvo = service.salvar(cliente);
		
		assertThat(clienteSalvo.getId()).isEqualTo(retorno.getId());
		assertThat(clienteSalvo.getNome()).isEqualTo(retorno.getNome());
		assertThat(clienteSalvo.getSexo()).isEqualTo(retorno.getSexo());
		assertThat(clienteSalvo.getDataNascimento()).isEqualTo(retorno.getDataNascimento());
		assertThat(clienteSalvo.getIdade()).isEqualTo(retorno.getIdade());
		assertThat(clienteSalvo.getCidade().getId()).isEqualTo(cidade.getId());
		
	}
	
	@Test
	@DisplayName("Deve lançar um erro ao tentar salvar um cliente com sexo inválido")
	public void valorInvalidoSexoTest() {
		
		Cliente cliente = criarCliente();
		cliente.setSexo("A");
		
		Throwable exception = Assertions.catchThrowable(() -> service.salvar(cliente));
		
		assertThat(exception)
			.isInstanceOf(RegraNegocioException.class)
			.hasMessage("Valor inválido para o sexo");
		
		verify(repository, never()).save(cliente);
	}
	
	@Test
	@DisplayName("Deve lançar um erro ao tentar salvar um cliente com cidade inválida")
	public void valorInvalidoCidadeTest() {
		
		Cliente cliente = criarCliente();
		
		when(cidadeRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Throwable exception = Assertions.catchThrowable(() -> service.salvar(cliente));
		
		assertThat(exception)
			.isInstanceOf(RegraNegocioException.class)
			.hasMessage("Cidade não encontrada");
	
		verify(repository, never()).save(cliente);
	}
	
	@Test
	@DisplayName("Deve retornar um cliente por Id")
	public void getPorIdTest() {
		
		Long id = 1l;
		Cliente cliente = criarCliente();
		cliente.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.of(cliente));
		
		Optional<Cliente> clienteEncontrado = service.getPorId(id);
		
		assertThat(clienteEncontrado.isPresent()).isTrue();
		assertThat(clienteEncontrado.get().getId()).isEqualTo(id);
		assertThat(clienteEncontrado.get().getNome()).isEqualTo(cliente.getNome());
		assertThat(clienteEncontrado.get().getSexo()).isEqualTo(cliente.getSexo());
		assertThat(clienteEncontrado.get().getDataNascimento()).isEqualTo(cliente.getDataNascimento());
		assertThat(clienteEncontrado.get().getIdade()).isEqualTo(cliente.getIdade());
		assertThat(clienteEncontrado.get().getCidade().getId()).isEqualTo(cliente.getId());
	}
	
	@Test
	@DisplayName("Deve retornar os clientes pelo Nome")
	public void getPorNomeTest() {
		
		Cliente cliente = criarCliente();
		cliente.setId(1l);
		
		List<Cliente> clientes = Arrays.asList(cliente);
		
		when(repository.findByNome(cliente.getNome())).thenReturn(clientes);
		
		List<Cliente> clientesEncontrado = service.getPorNome(cliente.getNome());
		
		assertThat(clientesEncontrado).isNotEmpty();
		assertThat(clientesEncontrado.get(0).getId()).isEqualTo(cliente.getId());
		assertThat(clientesEncontrado.get(0).getNome()).isEqualTo(cliente.getNome());
		assertThat(clientesEncontrado.get(0).getSexo()).isEqualTo(cliente.getSexo());
		assertThat(clientesEncontrado.get(0).getDataNascimento()).isEqualTo(cliente.getDataNascimento());
		assertThat(clientesEncontrado.get(0).getIdade()).isEqualTo(cliente.getIdade());
		assertThat(clientesEncontrado.get(0).getCidade().getId()).isEqualTo(cliente.getId());
	}
	
	@Test
	@DisplayName("Deve deletar um cliente")
	public void deletarTest() {
		
		Cliente cliente = criarCliente();
		cliente.setId(1l);
		
		service.deletar(cliente);
		
		verify(repository, atLeastOnce()).delete(cliente);
	}
	
	@Test
	@DisplayName("Deve atualizar o nome de um cliente")
	public void atualizarTest() {
		
		Cliente cliente = criarCliente();
		
		Cliente retorno = criarCliente();
		retorno.setId(1l);
		
		when(repository.save(cliente)).thenReturn(retorno);
		
		Cliente clienteSalvo = service.atualizar(cliente);
		
		assertThat(clienteSalvo.getId()).isEqualTo(retorno.getId());
		assertThat(clienteSalvo.getNome()).isEqualTo(retorno.getNome());
		assertThat(clienteSalvo.getSexo()).isEqualTo(retorno.getSexo());
		assertThat(clienteSalvo.getDataNascimento()).isEqualTo(retorno.getDataNascimento());
		assertThat(clienteSalvo.getIdade()).isEqualTo(retorno.getIdade());
		assertThat(clienteSalvo.getCidade().getId()).isEqualTo(retorno.getCidade().getId());
		
	}
}
