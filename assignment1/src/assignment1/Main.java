package assignment1;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Inserisci precisione da raggiungere: ");
		double accuracy = scanner.nextDouble();
		
		System.out.println("Inserisci tempo massimo per il calcolo: ");
		long maxTime = scanner.nextLong();
		
		scanner.close();
		
		PICalculator PICalculator = new PICalculator(accuracy);
		Thread PIThread = new Thread(PICalculator);
		
		System.out.println("Calcolo avviato");
		PIThread.start();
		try {
			PIThread.join(maxTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(PIThread.isAlive()) {
			PIThread.interrupt();
			System.out.println("Calcolo non terminato");
		}
	}
}
