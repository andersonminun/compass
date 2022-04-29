package io.github.anderson.api.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "nome", "sexo", "data_nascimento", "idade", "cidade"})
public class InformacaoClienteDTO {

	private Long id;
	
	private String nome;
	
	private String sexo;
	
	@JsonProperty("data_nascimento")
	@JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate dataNascimento;
	
	private Integer idade;
	
	private CidadeDTO cidade;
	
	public InformacaoClienteDTO() {
		
	}

	public InformacaoClienteDTO(Long id, String nome, String sexo, LocalDate dataNascimento, Integer idade,
			CidadeDTO cidade) {
		this.id = id;
		this.nome = nome;
		this.sexo = sexo;
		this.dataNascimento = dataNascimento;
		this.idade = idade;
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

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public CidadeDTO getCidade() {
		return cidade;
	}

	public void setCidade(CidadeDTO cidade) {
		this.cidade = cidade;
	}

	@Override
	public String toString() {
		return "InformacaoClienteDTO [id=" + id + ", nome=" + nome + ", sexo=" + sexo + ", dataNascimento="
				+ dataNascimento + ", idade=" + idade + ", cidade=" + cidade + "]";
	}
	
}
