package assignment9.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
 * Autore: Alessandro Puccia
 * Matricola 547462
 * Corso A
 */

public interface CongressService extends Remote {
	public String registerSpeaker(int day, int session, String speakerName) throws RemoteException;
	
	public String getCongressProgram(int day) throws RemoteException;
}
