package assignment9.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/*
 * Autore: Alessandro Puccia
 * Matricola 547462
 * Corso A
 */

public class MainClass {
	private final static int PORT = 8000;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CongressServiceImpl congressService = new CongressServiceImpl();
		CongressService stubCongress;
		Registry registry;
		
		try {
			stubCongress = (CongressService) UnicastRemoteObject.exportObject(congressService, 0);
			
			LocateRegistry.createRegistry(PORT);
			registry = LocateRegistry.getRegistry(PORT);
			
			registry.rebind("CONGRESS_SERVICE", stubCongress);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
