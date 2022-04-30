package io.github.anderson.api.dto;

import javax.validation.constraints.NotEmpty;

public class AtualizarNomeClienteDTO {

	@NotEmpty(message = "{nome.obrigatorio}")
	private String nome;
	
	public AtualizarNomeClienteDTO() {
		
	}

	public AtualizarNomeClienteDTO(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "AtualizarNomeClienteDTO [nome=" + nome + "]";
	}
	
}
