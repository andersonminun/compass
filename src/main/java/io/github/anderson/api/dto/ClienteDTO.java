package io.github.anderson.api.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import io.github.anderson.util.Constantes;

public class ClienteDTO {

	private Long id;
	
	@NotEmpty(message = "Campo nome é obrigatório")
	private String nome;
	
	@JsonSetter(nulls = Nulls.SKIP)
	private String sexo = Constantes.SEXO_NAO_INFORMADO;
	
	@JsonProperty("data_nascimento")
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonSerialize(using = LocalDateSerializer.class)
	@NotNull(message = "Campo data de nascimento é obrigatório")
    private LocalDate dataNascimento;
	
	@NotNull(message = "Informe o código da cidade")
	private Long cidade;
	
	public ClienteDTO() {
		
	}

	public ClienteDTO(String nome, String sexo, LocalDate dataNascimento, Long cidade) {
		this.nome = nome;
		this.sexo = sexo;
		this.dataNascimento = dataNascimento;
		this.cidade = cidade;
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

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Long getCidade() {
		return cidade;
	}

	public void setCidade(Long cidade) {
		this.cidade = cidade;
	}

	@Override
	public String toString() {
		return "ClienteDTO [id=" + id + ", nome=" + nome + ", sexo=" + sexo + ", dataNascimento=" + dataNascimento
				+ ", cidade=" + cidade + "]";
	}
	
}
