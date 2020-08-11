package assignment9.server;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;

/*
 * Autore: Alessandro Puccia
 * Matricola 547462
 * Corso A
 */

public class CongressServiceImpl extends RemoteServer implements CongressService{
	private static final long serialVersionUID = 1L;
	
	private static final String WRONG_DAY = "Giorno non valido";
	private static final String WRONG_SPEAKER = "Speaker non valido";
	
	private final CongressDay[] daysCongress;

	public CongressServiceImpl() {
		daysCongress = new CongressDay[3];
		daysCongress[0] = new CongressDay(1);
		daysCongress[1] = new CongressDay(2);
		daysCongress[2] = new CongressDay(3);
	}
	
	@Override
	public String registerSpeaker(int day, int session, String speakerName) throws RemoteException {
		// TODO Auto-generated method stub
		if (day < 1 || day > 3) {
			return WRONG_DAY + " (nome: " + speakerName + " - giorno: " + day + " - sessione: " + session + ")";
		}
		
		if (speakerName == null) {
			return WRONG_SPEAKER + " (nome: " + speakerName + " - giorno: " + day + " - sessione: " + session + ")";
		}
		
		return daysCongress[day - 1].insertSpeech(session, speakerName);
	}

	@Override
	public String getCongressProgram(int day) throws RemoteException {
		// TODO Auto-generated method stub
		if (day > 3) {
			return WRONG_DAY + " (giorno: " + day + ")";
		}
		else if (day < 0){
			return "\nTUTTI I GIORNI\n" + daysCongress[0].getProgram() + "\n" + daysCongress[1].getProgram() + "\n" + daysCongress[2].getProgram();
		}
		else {
			return "\nGIORNO " + day + "\n" + daysCongress[day - 1].getProgram();
		}
	}

	
}
