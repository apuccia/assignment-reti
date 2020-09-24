package assignment2;

import java.util.Scanner;

/*
 * Autore: Alessandro Puccia corso A
 * Matricola: 547462
 */

public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int capacity, groupSize;
		char stop = 'n';

		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Inserisci dimensione massima della prima coda: ");
		capacity = scanner.nextInt();
		
		System.out.println("Inserisci la dimensione del gruppo per la seconda coda: ");
		groupSize = scanner.nextInt();
		
		
		/*
		 * Il thread spawnerThread si occuperà di produrre i task e inserirli nella prima coda.
		 * Il thread officeThread si occuperà di estrarre i task dalla prima coda e inserirli
		 * nella coda del pool.
		 */
		PostOffice postOffice = new PostOffice(capacity, groupSize);
		TaskSpawner taskSpawner = new TaskSpawner(postOffice);
		Thread spawnerThread = new Thread(taskSpawner);
		Thread officeThread = new Thread(postOffice);
		
		officeThread.start();
		spawnerThread.start();
		
		while (stop != 's') {
			System.out.println("Premi s per interrompere: ");
			stop = scanner.next().charAt(0);
		}

		scanner.close();
		
		// Interrompo lo spawner di task.
		spawnerThread.interrupt();
		try {
			spawnerThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Task spawner chiuso.");
		

		// Interrompo il thread dell'ufficio.
		officeThread.interrupt();
		try {
			officeThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Ufficio postale chiuso.");
	}
	
}
