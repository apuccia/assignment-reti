package assignment9.server;

import java.util.Vector;

/*
 * Autore: Alessandro Puccia
 * Matricola 547462
 * Corso A
 */

public class CongressDay {
	private static final String WRONG_SESSION = "Sessione sbagliata";
	private static final String SESSION_COMPLETE = "Sessione al completo";
	private static final String SPEAKER_ALREADY_REGISTERED = "Speaker gia' registrato";
	
	private static final String SPEAKER_REGISTERED = "Speaker registrato";
	
	private Vector<Vector<String>> congressDay = new Vector<Vector<String>>();
	private final int day;
	
	public CongressDay(int day) {
		congressDay = new Vector<Vector<String>>();
		this.day = day;
		
		for (int i = 0; i <= 12; i++) {
			congressDay.add(new Vector<String>());
		}
		
		Vector<String> first = congressDay.get(0);
		
		first.add("Sessione");
		for (int i = 1; i <= 5; i++) {
			first.add("Intervento " + i);
		}
		
		for (int i = 1; i <= 12; i++) {
			for (int j = 0; j < 5; j++) {
				if (j == 0) {
					congressDay.get(i).add("S" + i);
				}
			}
		}
	}
	
	public String insertSpeech(int session, String speakerName) {
		if (session < 1 || session > 12) {
			return WRONG_SESSION + " (nome: " + speakerName + " - sessione: " + session + ")";
		}

		Vector<String> vecSession = congressDay.get(session);
		if (vecSession.size() == 6) {
			return SESSION_COMPLETE + " (nome: " + speakerName + " - giorno: " + day + " - sessione: " + session + ")";
		}
		if (vecSession.contains(speakerName)) {
			return SPEAKER_ALREADY_REGISTERED + " (nome: " + speakerName + " - giorno: " + day + " - sessione: " + session + ")";
		}
			
		vecSession.add(speakerName);
			
		return SPEAKER_REGISTERED + " (nome: " + speakerName + " - giorno: " + day + " - sessione: " + session + ")";
	}
	
	public String getProgram() {
		return this.toString();
	}
	
	@Override
	public String toString() {
		return congressDay.get(0).toString() + "\n" +
			congressDay.get(1).toString() + "\n" +
			congressDay.get(2).toString() + "\n" +
			congressDay.get(3).toString() + "\n" +
			congressDay.get(4).toString() + "\n" +
			congressDay.get(5).toString() + "\n" +
			congressDay.get(6).toString() + "\n" +
			congressDay.get(7).toString() + "\n" +
			congressDay.get(8).toString() + "\n" +
			congressDay.get(9).toString() + "\n" +
			congressDay.get(10).toString() + "\n" +
			congressDay.get(11).toString() + "\n" +
			congressDay.get(12).toString() + "\n";
	}
}
