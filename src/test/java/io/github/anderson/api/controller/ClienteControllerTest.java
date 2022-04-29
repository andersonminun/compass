package io.github.anderson.api.controller;

import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.anderson.api.dto.AtualizarNomeClienteDTO;
import io.github.anderson.api.dto.ClienteDTO;
import io.github.anderson.api.exception.RegraNegocioException;
import io.github.anderson.model.entity.Cidade;
import io.github.anderson.model.entity.Cliente;
import io.github.anderson.service.ClienteService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ClienteController.class)
@AutoConfigureMockMvc
public class ClienteControllerTest {

	private static String CLIENTE_API = "/api/clientes";
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
	private ClienteService service;
	
	private Cidade criarCidade() {
		
		Cidade cidade = new Cidade("Recife", "Pernambuco");
		cidade.setId(1l);
		
		return cidade;
	}
	
	private Cliente criarCliente() {
		
		Cidade cidade = criarCidade();
		
		return new Cliente("Anderson", "M", LocalDate.of(1989, 1, 19), cidade);
	}
	
	private ClienteDTO criarClienteDTO() {
		
		return new ClienteDTO("Anderson", "M", LocalDate.of(1989, 1, 19), 1l);
	}
	
	@Test
	@DisplayName("Deve salvar um cliente")
	public void salvarTest() throws Exception {
		
		ClienteDTO dto = criarClienteDTO();
		Cliente clienteSalvo = criarCliente();
		clienteSalvo.setId(1l);
		
		given(service.salvar(Mockito.any(Cliente.class))).willReturn(clienteSalvo);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(CLIENTE_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(1l))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(dto.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("sexo").value(dto.getSexo()))
			.andExpect(MockMvcResultMatchers.jsonPath("cidade").value(dto.getCidade()));
	}
	
	@Test
	@DisplayName("Deve lançar erro de validação quando não houver dados suficiente")
	public void salvarCidadeInvalidaTest() throws Exception {
		
		String json = new ObjectMapper().writeValueAsString(new ClienteDTO());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(CLIENTE_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(3)));
	}
	
	@Test
	@DisplayName("Deve lançar erro ao tentar salvar cliente com cidade inválida")
	public void salvarClienteCidadeInvalidaTest() throws Exception {
		
		ClienteDTO dto = criarClienteDTO();
		dto.setCidade(2l);
		
		String mensagem = "Cidade não encontrada";
		
		given(service.salvar(Mockito.any(Cliente.class)))
			.willThrow(new RegraNegocioException(mensagem));
	
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(CLIENTE_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(mensagem));
	}
	
	@Test
	@DisplayName("Deve encontrar o cliente pelo Id")
	public void getPorIdTest() throws Exception {
		
		Long id = 1l;
		Cliente clienteEncontrado = criarCliente();
		clienteEncontrado.setId(id);
		
		given(service.getPorId(id)).willReturn(Optional.of(clienteEncontrado));
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(CLIENTE_API.concat("/" + id))
				.accept(MediaType.APPLICATION_JSON);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(clienteEncontrado.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("sexo").value(clienteEncontrado.getSexo()))
			.andExpect(MockMvcResultMatchers.jsonPath("idade").value(clienteEncontrado.getIdade()));
	}
	
	@Test
	@DisplayName("Deve lançar um erro quando não encontrar um cliente")
	public void getPorIdClienteInexistenteTest() throws Exception {
		
		given(service.getPorId(Mockito.anyLong())).willReturn(Optional.empty());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(CLIENTE_API.concat("/" + 1l))
				.accept(MediaType.APPLICATION_JSON);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNotFound())
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value("Cliente não encontrado"));
	}
	
	@Test
	@DisplayName("Deve encontrar os clientes por nome")
	public void getPorNomeTest() throws Exception {
		
		Cliente cliente = criarCliente();
		cliente.setId(1l);
		List<Cliente> clientes = Arrays.asList(cliente);
		
		given(service.getPorNome(Mockito.anyString())).willReturn(clientes);
		
		String query = String.format("?nome=%s", cliente.getNome());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(CLIENTE_API.concat(query))
				.accept(MediaType.APPLICATION_JSON);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("[0].id").value(cliente.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("[0].nome").value(cliente.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("[0].sexo").value(cliente.getSexo()))
			.andExpect(MockMvcResultMatchers.jsonPath("[0].idade").value(cliente.getIdade()));
	}
	
	@Test
	@DisplayName("Deve deletar um cliente")
	public void deletarTest() throws Exception {
		
		Long id = 1l;
		Cliente cliente = criarCliente();
		cliente.setId(id);
		
		given(service.getPorId(id)).willReturn(Optional.of(cliente));
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(CLIENTE_API.concat("/" + id));
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	@DisplayName("Deve lançar erro ao tentar deletar um cliente inexistente")
	public void deletarClienteInexistenteTest() throws Exception {
		
		given(service.getPorId(Mockito.anyLong())).willReturn(Optional.empty());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(CLIENTE_API.concat("/" + 1l));
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNotFound())
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value("Cliente não encontrado"));
	}
	
	@Test
	@DisplayName("Deve atualizar o nome do cliente")
	public void atualizarNomeTest() throws Exception {
		
		Long id = 1l;
		Cliente cliente = criarCliente();
		cliente.setId(id);
		AtualizarNomeClienteDTO dto = new AtualizarNomeClienteDTO("Arthur");
		Cliente clienteAtualizado = criarCliente();
		clienteAtualizado.setId(id);
		clienteAtualizado.setNome(dto.getNome());
		
		given(service.getPorId(id)).willReturn(Optional.of(cliente));
		given(service.atualizar(cliente)).willReturn(clienteAtualizado);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.patch(CLIENTE_API.concat("/" + id))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(1l))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(clienteAtualizado.getNome()));
	}
	
	@Test
	@DisplayName("Deve lançar erro ao tentar atualizar um cliente inexistente")
	public void atualizarClienteInexistenteTest() throws Exception {
		
		AtualizarNomeClienteDTO dto = new AtualizarNomeClienteDTO("Arthur");
		
		given(service.getPorId(Mockito.anyLong())).willReturn(Optional.empty());
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.patch(CLIENTE_API.concat("/" + 1l))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNotFound())
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value("Cliente não encontrado"));
	}
	
	@Test
	@DisplayName("Deve lançar erro de validação quando não houver dados suficiente na atualização")
	public void atalizarNomeInvalidoTest() throws Exception {
		
		String json = new ObjectMapper().writeValueAsString(new AtualizarNomeClienteDTO());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.patch(CLIENTE_API.concat("/" + 1l))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)));
	}
}
