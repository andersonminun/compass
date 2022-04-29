package io.github.anderson.util;

import java.time.LocalDate;
import java.time.Period;

public class CalculadoraIdade {

	/**
	 * Calcula a idade a partir da data informada
	 */
	public static int calcular(LocalDate dataNascimento) {
        
        return Period.between(dataNascimento, LocalDate.now()).getYears();
        
    }
}
