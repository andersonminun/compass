package io.github.anderson.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import io.github.anderson.api.ApiErrors;
import io.github.anderson.api.exception.RegraNegocioException;

@RestControllerAdvice
public class ApplicationControllerAdvice {

	/**
    * Trata os erros de mal formação do JSON recebido
    */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleMethodNotValidExcpetion(MethodArgumentNotValidException ex) {
		
		List<String> errors = ex.getBindingResult().getAllErrors()
				.stream()
				.map(erro -> erro.getDefaultMessage())
				.collect(Collectors.toList());
		
		return new ApiErrors(errors);
	}
	
	/**
	 * Trata erros gerais, recebendo o status HTTP a ser retornado e uma mensagem
	 */
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiErrors> handleResponseStatusException(ResponseStatusException ex) {
		return new ResponseEntity<ApiErrors>(new ApiErrors(ex.getReason()), ex.getStatus());
	}
	
	/**
	 * Trata erros de regra de negócio
	 */
	@ExceptionHandler(RegraNegocioException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleRegraNegocioException(RegraNegocioException ex) {
		return new ApiErrors(ex.getMessage());
	}
}
