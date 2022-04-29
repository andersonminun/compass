package io.github.anderson.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.anderson.api.dto.AtualizarNomeClienteDTO;
import io.github.anderson.api.dto.CidadeDTO;
import io.github.anderson.api.dto.ClienteDTO;
import io.github.anderson.api.dto.InformacaoClienteDTO;
import io.github.anderson.model.entity.Cidade;
import io.github.anderson.model.entity.Cliente;
import io.github.anderson.service.ClienteService;

@RestController
@RequestMapping("api/clientes")
public class ClienteController {

	private ClienteService service;

	public ClienteController(ClienteService service) {
		this.service = service;
	}
	
	/**
	 * Converte o DTO para a entidade Cliente
	 */
	private Cliente converterParaObj(ClienteDTO dto) {
		
		Cidade cidade = new Cidade();
		cidade.setId(dto.getCidade());
		
		Cliente cliente = new Cliente(dto.getNome(), dto.getSexo(), dto.getDataNascimento(), cidade);
		
		return cliente;
	}
	
	/**
	 * Converte a entidade Cliente no DTO
	 */
	private InformacaoClienteDTO converterParaInfo(Cliente cliente) {
		
		CidadeDTO cidadeDTO = new CidadeDTO(cliente.getCidade().getId(), cliente.getCidade().getNome(),
				cliente.getCidade().getEstado());
		
		return new InformacaoClienteDTO(cliente.getId(), cliente.getNome(), cliente.getSexo(), 
				cliente.getDataNascimento(), cliente.getIdade(), cidadeDTO);
	}
	
	/**
	 * Salva um novo cliente
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ClienteDTO salvar(@RequestBody @Valid ClienteDTO dto) {
		
		Cliente cliente = converterParaObj(dto);
		cliente = service.salvar(cliente);
		
		dto.setId(cliente.getId());
		
		return dto;
	}
	
	/**
	 * Retorna um cliente pelo Id
	 */
	@GetMapping("{id}")
	public InformacaoClienteDTO getPorId(@PathVariable Long id) {
		
		Cliente cliente = service.getPorId(id)
				.orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
		
		return converterParaInfo(cliente);
	}
	
	/**
	 * Retorna um cliente pelo Nome
	 */
	@GetMapping
	public List<InformacaoClienteDTO> getPorNome(String nome) {
		
		List<Cliente> clientes = service.getPorNome(nome);
		
		return clientes
				.stream()
				.map(cliente -> converterParaInfo(cliente))
				.collect(Collectors.toList());
	}
	
	/**
	 * Deleta um cliente pelo Id
	 */
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		
		Cliente cliente = service.getPorId(id)
				.orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
		
		service.deletar(cliente);
	}
	
	/**
	 * Atualiza o nome do cliente pelo Id
	 */
	@PatchMapping("{id}")
	public InformacaoClienteDTO atualizarNome(@PathVariable Long id, @RequestBody @Valid AtualizarNomeClienteDTO dto) {
		
		Cliente cliente = service.getPorId(id)
				.orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
		
		cliente.setNome(dto.getNome());
		
		Cliente clienteSalvo = service.atualizar(cliente);
		
		return converterParaInfo(clienteSalvo);
	}
}
