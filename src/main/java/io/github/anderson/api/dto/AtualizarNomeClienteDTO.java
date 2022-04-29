package io.github.anderson.api.dto;

import javax.validation.constraints.NotEmpty;

public class AtualizarNomeClienteDTO {

	@NotEmpty(message = "Campo nome é obrigatório")
	private String nome;
	
	public AtualizarNomeClienteDTO() {
		
	}

	public AtualizarNomeClienteDTO(@NotEmpty(message = "Campo nome é obrigatório") String nome) {
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
