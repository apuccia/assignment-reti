package assignment8.client;

import java.io.PrintWriter;

/*
	Autore: Alessandro Puccia
	Matricola: 547462
	Corso A
*/

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PrintWriter clientPrintWriter = new PrintWriter(System.out, true);
		
		String address = null;
		int serverPort = 0;

		// leggo l'indirizzo del server
		try {
			address = args[0];
		} catch (Exception e) {
			clientPrintWriter.println("ERR -arg 0");
			System.exit(1);
		}
		
		// leggo la porta di ascolto del server
		try {
			serverPort = Integer.parseInt(args[1]);
		} catch (Exception e1) {
			clientPrintWriter.println("ERR -arg 1");
			System.exit(1);
		}
		
		PingClient pingClient = new PingClient(address, serverPort);
		Thread pingThread = new Thread(pingClient);
		
		pingThread.start();
		
		try {
			pingThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		clientPrintWriter.println("Terminato");
	}
}
