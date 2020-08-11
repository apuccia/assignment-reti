package assignment5;

import java.util.concurrent.locks.ReentrantLock;

/*
 * Autore: Alessandro Puccia
 * Corso A
 * Matricola: 547462
 */

public class Contatore {
	private final ReentrantLock contatoriLock;
	private int totalBonifici;
	private int totalAccrediti;
	private int totalBollettini;
	private int totalF24;
	private int totalPagoBancomat;
	
	public Contatore() {
		contatoriLock = new ReentrantLock();
		totalBonifici = totalAccrediti = totalBollettini = totalF24 = totalPagoBancomat = 0;
	}
	
	public void accessContatori() {
		contatoriLock.lock();
	}
	
	public void releaseContatori() {
		contatoriLock.unlock();
	}
	
	public void increaseBonifici (int numeroBonifici) {
		totalBonifici += numeroBonifici;
	}
	
	public void increaseAccrediti(int numeroAccrediti) {
		totalAccrediti += numeroAccrediti;
	}
	
	public void increaseBollettini(int numeroBollettini) {
		totalBollettini += numeroBollettini;
	}
	
	public void increaseF24(int numeroF24) {
		totalF24 += numeroF24;
	}
	
	public void increasePagoBancomat(int numeroPagoBancomat) {
		totalPagoBancomat += numeroPagoBancomat;
	}
	
	public void printTotal() {
		// chiamata solamente dal produttore alla fine, niente concorrenza.
		System.out.println("Numero di bonifici totali: " + totalBonifici);
		System.out.println("Numero di accrediti totali: " + totalAccrediti);
		System.out.println("Numero di bollettini totali: " + totalBollettini);
		System.out.println("Numero di F24 totali: " + totalF24);
		System.out.println("Numero di PagoBancomat totali: " + totalPagoBancomat + "\n");
	}

}
