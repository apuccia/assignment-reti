package assignment6;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
	Autore: Alessandro Puccia
	Matricola: 547462 
	Corso A
*/

public class MainClass {
	static final int PORT = 6789;
	
	public static void main(String[] args) {
		int requests = Integer.parseInt(args[0]);
		PrintWriter mainPrinter = new PrintWriter(System.out, true);
		
		// mi metto in ascolto sulla porta 6789
		try (ServerSocket passiveSocket = new ServerSocket(PORT)) {
			while (requests > 0) {
				// quando arriva una nuova richiesta creo un nuovo thread a cui passo il socket attivo.
				Socket activeSocket = passiveSocket.accept();
				mainPrinter.println("E' arrivata una nuova richiesta\n");
				
				HTTPWorker worker = new HTTPWorker(activeSocket);
				Thread workerThread = new Thread(worker);
				
				workerThread.start();
				workerThread.join();
				
				requests--;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mainPrinter.println("Richieste terminate\n");
	}
}
