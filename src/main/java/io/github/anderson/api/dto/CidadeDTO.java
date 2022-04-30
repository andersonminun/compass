package io.github.anderson.api.dto;

import javax.validation.constraints.NotEmpty;

public class CidadeDTO {

	private Long id;
	
	@NotEmpty(message = "{nome.obrigatorio}")
	private String nome;
	
	@NotEmpty(message = "{estado.obrigatorio}")
	private String estado;
	
	public CidadeDTO() {
		
	}

	public CidadeDTO(String nome, String estado) {
		this.nome = nome;
		this.estado = estado;
	}
	
	public CidadeDTO(Long id, String nome, String estado) {
		this.id = id;
		this.nome = nome;
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "CidadeDTO [id=" + id + ", nome=" + nome + ", estado=" + estado + "]";
	}
	
}
