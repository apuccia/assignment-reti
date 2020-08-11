package assignment9.client;

import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import assignment9.server.CongressService;

/*
 * Autore: Alessandro Puccia
 * Matricola 547462
 * Corso A
 */

public class MainClass {
	private final static int PORT = 8000;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Registry registry = null;
		CongressService congress = null;
		PrintWriter clientPrintWriter = new PrintWriter(System.out, true);
		
		try {
			registry = LocateRegistry.getRegistry(PORT);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		try {
			congress = (CongressService) registry.lookup("CONGRESS_SERVICE");
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			clientPrintWriter.println(congress.registerSpeaker(1, 2, "pippo"));
			clientPrintWriter.println(congress.registerSpeaker(2, 5, "pluto"));
			clientPrintWriter.println(congress.registerSpeaker(1, 1, "paperino"));
			clientPrintWriter.println(congress.registerSpeaker(3, 4, "topolino"));
			clientPrintWriter.println(congress.registerSpeaker(2, 4, "minnie"));
			clientPrintWriter.println(congress.registerSpeaker(3, 2, "archimede"));
			
			clientPrintWriter.println(congress.registerSpeaker(2, 2, "paperino"));
			clientPrintWriter.println(congress.registerSpeaker(3, 3, "topolino"));
			clientPrintWriter.println(congress.registerSpeaker(3, 5, "minnie"));
			clientPrintWriter.println(congress.registerSpeaker(4, 3, "archimede"));
			clientPrintWriter.println(congress.registerSpeaker(3, 2, "pippo"));
			clientPrintWriter.println(congress.registerSpeaker(1, 3, "pluto"));
			clientPrintWriter.println(congress.registerSpeaker(1, 3, "paperino"));
			
			clientPrintWriter.println(congress.registerSpeaker(1, 2, "pluto"));
			clientPrintWriter.println(congress.registerSpeaker(2, 2, "paperino"));
			clientPrintWriter.println(congress.registerSpeaker(1, 3, "topolino"));
			clientPrintWriter.println(congress.registerSpeaker(1, 5, "minnie"));
			clientPrintWriter.println(congress.registerSpeaker(1, 3, "archimede"));
			
			clientPrintWriter.println(congress.registerSpeaker(5, 20, "paperino"));
			clientPrintWriter.println(congress.registerSpeaker(1, 2, "topolino"));
			clientPrintWriter.println(congress.registerSpeaker(3, 4, "minnie"));
			clientPrintWriter.println(congress.registerSpeaker(1, 2, "archimede"));
			clientPrintWriter.println(congress.registerSpeaker(2, 2, "pippo"));
			clientPrintWriter.println(congress.registerSpeaker(1, 3, "pluto"));
			clientPrintWriter.println(congress.registerSpeaker(1, 2, "paperino"));
			
			clientPrintWriter.println(congress.registerSpeaker(1, 12, "pippo"));
			clientPrintWriter.println(congress.registerSpeaker(2, 10, "pluto"));
			clientPrintWriter.println(congress.registerSpeaker(1, 6, "paperino"));
			clientPrintWriter.println(congress.registerSpeaker(3, 9, "topolino"));
			clientPrintWriter.println(congress.registerSpeaker(2, 4, "minnie"));
			clientPrintWriter.println(congress.registerSpeaker(3, 8, "archimede"));
			
			clientPrintWriter.println(congress.registerSpeaker(2, 11, "paperino"));
			clientPrintWriter.println(congress.registerSpeaker(3, 10, "topolino"));
			clientPrintWriter.println(congress.registerSpeaker(3, 14, "minnie"));
			clientPrintWriter.println(congress.registerSpeaker(4, 11, "archimede"));
			clientPrintWriter.println(congress.registerSpeaker(3, 8, "pippo"));
			clientPrintWriter.println(congress.registerSpeaker(1, 7, "pluto"));
			clientPrintWriter.println(congress.registerSpeaker(1, 9, "paperino"));
			
			clientPrintWriter.println(congress.registerSpeaker(1, 6, "pluto"));
			clientPrintWriter.println(congress.registerSpeaker(2, 10, "paperino"));
			clientPrintWriter.println(congress.registerSpeaker(1, 11, "topolino"));
			clientPrintWriter.println(congress.registerSpeaker(1, 9, "minnie"));
			clientPrintWriter.println(congress.registerSpeaker(1, 1, "archimede"));
			
			clientPrintWriter.println(congress.registerSpeaker(5, 10, "paperino"));
			clientPrintWriter.println(congress.registerSpeaker(1, 8, "topolino"));
			clientPrintWriter.println(congress.registerSpeaker(3, 9, "minnie"));
			clientPrintWriter.println(congress.registerSpeaker(1, 7, "archimede"));
			clientPrintWriter.println(congress.registerSpeaker(2, 9, "pippo"));
			clientPrintWriter.println(congress.registerSpeaker(1, 6, "pluto"));
			clientPrintWriter.println(congress.registerSpeaker(1, 5, "paperino"));
			clientPrintWriter.println(congress.registerSpeaker(1, 2, "paperoga"));

			clientPrintWriter.println(congress.getCongressProgram(-1));
			clientPrintWriter.println(congress.getCongressProgram(1));
			clientPrintWriter.println(congress.getCongressProgram(2));
			clientPrintWriter.println(congress.getCongressProgram(3));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
