package assignment5;

import java.util.ArrayList;

/*
 * Autore: Alessandro Puccia
 * Corso A
 * Matricola: 547462
 */

public class ContoCorrente{
	private final String nomeCorrentista;
	private final ArrayList<Movimento> movimenti;
	
	public ContoCorrente(String nomeCorrentista) {
		this.nomeCorrentista = nomeCorrentista;
		movimenti = new ArrayList<Movimento>();
	}
	
	public ContoCorrente(String nomeCorrentista, ArrayList<Movimento> movimenti) {
		this.nomeCorrentista = nomeCorrentista;
		this.movimenti = movimenti;
	}
	
	public String getNomeCorrentista() {
		return nomeCorrentista;
	}
	
	public void nuovoMovimento(Movimento movimento) {
		movimenti.add(movimento);
	}
	
	public ArrayList<Movimento> getMovimenti() {
		return movimenti;
	}
	
	@Override
	public String toString() {
		return "Nome: " + nomeCorrentista + "Movimenti: " + movimenti;
	}
}
