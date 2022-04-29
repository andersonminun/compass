package io.github.anderson.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.anderson.api.dto.CidadeDTO;
import io.github.anderson.model.entity.Cidade;
import io.github.anderson.service.CidadeService;

@RestController
@RequestMapping("api/cidades")
public class CidadeController {

	private CidadeService service;

	public CidadeController(CidadeService service) {
		this.service = service;
	}
	
	/**
	 * Converte o DTO na entidade Cidade
	 */
	private Cidade converterParaObj(CidadeDTO dto) {
		
		return new Cidade(dto.getNome(), dto.getEstado());
	}
	
	/**
	 * Converte a entidade Cidade no DTO
	 */
	private CidadeDTO converterParaDTO(Cidade cidade) {
		
		CidadeDTO dto = new CidadeDTO(cidade.getNome(), cidade.getEstado());
		dto.setId(cidade.getId());
		
		return dto;
	}
	
	/**
	 * Salva uma nova cidade
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeDTO salvar(@RequestBody @Valid CidadeDTO dto) {
		
		Cidade cidade = converterParaObj(dto);
		cidade = service.salvar(cidade);
		
		dto.setId(cidade.getId());
		
		return dto;
	}
	
	/**
	 * Retorna uma lista de cidades por filtro
	 */
	@GetMapping
	public List<CidadeDTO> getPorFiltro(CidadeDTO dto) {
		
		Cidade filtro = converterParaObj(dto);
		
		List<Cidade> cidades = service.getPorFiltro(filtro);
		
		return cidades
				.stream()
				.map(cidade -> converterParaDTO(cidade))
				.collect(Collectors.toList());
	}
}
