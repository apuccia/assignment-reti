package assignment5;

import java.time.LocalDate;

/*
 * Autore: Alessandro Puccia
 * Corso A
 * Matricola: 547462
 */

public class Movimento {
	
	private final LocalDate dataMovimento;
	public enum Causale{Bonifico, Accredito, Bollettino, F24, PagoBancomat};
	private final Causale tipoCausale;
	
	public Movimento(Causale causale, LocalDate dataMovimento) {
		tipoCausale = causale;
		this.dataMovimento = dataMovimento;
	}
	
	public Causale getCausale() {
		return tipoCausale;
	}
	
	public LocalDate getDate() {
		return dataMovimento;
	}
	
	@Override
	public String toString() {
		return "\nCausale: " + tipoCausale + " Data: " + dataMovimento;
	}
	
}
