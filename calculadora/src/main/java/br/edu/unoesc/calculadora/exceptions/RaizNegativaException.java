package br.edu.unoesc.calculadora.exceptions;

@SuppressWarnings("serial")
public class RaizNegativaException extends RuntimeException {
	public RaizNegativaException(String msg) {
		super(msg);
	}
}