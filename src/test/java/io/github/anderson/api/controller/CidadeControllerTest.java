package io.github.anderson.api.controller;

import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;

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

import io.github.anderson.api.dto.CidadeDTO;
import io.github.anderson.api.exception.RegraNegocioException;
import io.github.anderson.model.entity.Cidade;
import io.github.anderson.service.CidadeService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CidadeController.class)
@AutoConfigureMockMvc
public class CidadeControllerTest {

	private static String CIDADE_API = "/api/cidades";
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
	private CidadeService service;
	
	private Cidade criarCidade() {
		
		return new Cidade("Recife", "Pernambuco");
	}
	
	private CidadeDTO criarCidadeDTO() {
		
		return new CidadeDTO("Recife", "Pernambuco");
	}

	@Test
	@DisplayName("Deve salvar uma cidade")
	public void salvarTest() throws Exception {
		
		CidadeDTO dto = criarCidadeDTO();
		Cidade cidadeSalva = criarCidade();
		cidadeSalva.setId(1l);
		
		given(service.salvar(Mockito.any(Cidade.class))).willReturn(cidadeSalva);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(CIDADE_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(1l))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(dto.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("estado").value(dto.getEstado()));
	}
	
	@Test
	@DisplayName("Deve lançar erro de validação quando não houver dados suficiente")
	public void salvarCidadeInvalidaTest() throws Exception {
		
		String json = new ObjectMapper().writeValueAsString(new CidadeDTO());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(CIDADE_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(2)));
	}
	
	@Test
	@DisplayName("Deve lançar erro ao tentar salvar cidade já existente")
	public void salvarCidadeExistenteTest() throws Exception {
		
		CidadeDTO dto = criarCidadeDTO();
		
		String mensagem = "Cidade já cadastrada";
		
		given(service.salvar(Mockito.any(Cidade.class)))
			.willThrow(new RegraNegocioException(mensagem));
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(CIDADE_API)
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
	@DisplayName("Deve filtrar as cidades")
	public void getPorFiltroTest() throws Exception {
		
		Cidade cidade = criarCidade();
		cidade.setId(1l);
		List<Cidade> cidades = Arrays.asList(cidade);
		
		given(service.getPorFiltro(Mockito.any(Cidade.class))).willReturn(cidades);
		
		String query = String.format("?nome=%s", cidade.getNome());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(CIDADE_API.concat(query))
				.accept(MediaType.APPLICATION_JSON);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("[0].id").value(cidade.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("[0].nome").value(cidade.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("[0].estado").value(cidade.getEstado()));
	}
}
