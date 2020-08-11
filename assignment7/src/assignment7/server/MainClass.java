package assignment7.server;

import java.io.PrintWriter;

/*
 * Autore: Alessandro Puccia
 * Matricola: 547462
 * Corso A
 */

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int numRequests = Integer.parseInt(args[0]);
		PrintWriter mainServerPrintWriter = new PrintWriter(System.out, true);
		
		Worker worker = new Worker(numRequests);
		Thread workerThread = new Thread(worker);
		
		workerThread.start();
		
		try {
			workerThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mainServerPrintWriter.println("Richieste terminate, chiusura server");
	}
}
