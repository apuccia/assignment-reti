package assignment8.server;

import java.io.PrintWriter;

/*
	Autore: Alessandro Puccia
	Matricola: 547462
	Corso A
*/

public class MainClass {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PrintWriter serverPrintWriter = new PrintWriter(System.out, true);
		
		int port = 0;
		long seed = 0;
		
		// leggo il numero di porta in cui il server si metterà in ascolto
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			serverPrintWriter.println("ERR -arg 0");
			System.exit(1);
		}
		
		// leggo il seed (se dato) che sarà utilizzato per scegliere se "perdere" un pacchetto e il delay di risposta al client
		try {
			seed = Integer.parseInt(args[1]);
		} catch (NumberFormatException e1) {
			serverPrintWriter.println("ERR -arg 1");
			System.exit(1);
		} catch(Exception e) {
		
		}

		// creo un thread che si occuperà delle richieste
		PingServer pingServer = new PingServer(port, seed);
		Thread pingThread = new Thread(pingServer);
		
		pingThread.start();
		
		try {
			pingThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		serverPrintWriter.println("Terminato");
	}
}
