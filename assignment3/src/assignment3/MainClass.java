package assignment3;

import java.util.Random;

/*
 * Autore: Alessandro Puccia
 * Corso A
 * Matricola: 547462
 */
public class MainClass {

	public static void main(String[] args) {
		int white, yellow, red;
		
		//numero pazienti codice rosso.
		red = Integer.parseInt(args[0]);
		
		//numero pazienti codice giallo.
		yellow = Integer.parseInt(args[1]);
		
		//numero pazienti codice bianco.
		white = Integer.parseInt(args[2]);
		
		int i;
		Reparto reparto = new Reparto();
		Random random = new Random();
		
		// Creo i pazienti in codice rosso e relativi thread.
		for (i = 0; i < red; i++) {
			Paziente paziente = new Paziente(random.nextInt(20), 1, reparto);
			Thread threadPaziente = new Thread (paziente);
			
			threadPaziente.start();
		}
		
		// Creo i pazienti in codice giallo e relativi thread.
		for (i = 0; i < yellow; i++) {
			Paziente paziente = new Paziente(random.nextInt(20), 2, random.nextInt(10), reparto);
			Thread threadPaziente = new Thread (paziente);
			
			threadPaziente.start();
		}
		
		// Creo i pazienti in codice bianco e relativi thread.
		for (i = 0; i < white; i++) {
			Paziente paziente = new Paziente(random.nextInt(20), 3, reparto);
			Thread threadPaziente = new Thread (paziente);
			
			threadPaziente.start();
		}
	}
}
