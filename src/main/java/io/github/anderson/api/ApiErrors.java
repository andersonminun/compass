package io.github.anderson.api;

import java.util.Arrays;
import java.util.List;

public class ApiErrors {

	private List<String> errors;
	
	public ApiErrors(List<String> errors) {
		this.errors = errors;
	}

	public ApiErrors(String mensagemErro) {
		this.errors = Arrays.asList(mensagemErro);
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}
