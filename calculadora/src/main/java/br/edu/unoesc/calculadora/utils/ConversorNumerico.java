package br.edu.unoesc.calculadora.utils;

public class ConversorNumerico {
	public static boolean ehNumerico(String strNumero) {
		if (strNumero == null) {
			return false;
		}
		
		// 10,65 => 10.65
		String numero = strNumero.replaceAll(",", ".");
		
		// Utilizando tratamento de exceções
		try {
			Double.parseDouble(numero);
			return true; // Significa que a string contém um Double válido
		} catch (NumberFormatException e) {
			return false;
		}
		
		// Utilizando expressões regulares
//		return numero.matches("[-+]?\\d*\\.?\\d*");
	}
	
	public static Double converterParaDouble(String strNumero) {
		if (strNumero == null) {
			return 0d;
		}
		
		String numero = strNumero.replaceAll(",", ".");
		if (ehNumerico(numero)) {
			return Double.parseDouble(numero);
		}
		
		return 0d;
	}
}
