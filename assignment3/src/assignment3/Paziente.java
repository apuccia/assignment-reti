package assignment3;

/*
 * Autore: Alessandro Puccia
 * Corso A
 * Matricola: 547462
 */

public class Paziente implements Runnable {
	private int visits, doctor, status;
	private Reparto reparto;
	
	//costruttore utilizzato per i pazienti in codice rosso e bianco.
	public Paziente (int k, int status, Reparto reparto) {
		visits = k;
		this.status = status;
		this.reparto = reparto;
		doctor = -1;
	}
	
	//costruttore utilizzato per i pazienti in codice giallo.
	public Paziente (int k, int status, int doctor, Reparto reparto) {
		visits = k;
		this.status = status;
		this.reparto = reparto;
		this.doctor = doctor;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stud

		while (visits > 0) {
			reparto.startVisita(this);
			try {
				System.out.println("Sono il paziente " + Thread.currentThread().getId() +
						" con codice " + status + " inizia la mia visita");
				Thread.sleep((long)(Math.random() * 1000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Sono il paziente " + Thread.currentThread().getId() +
					" con codice " + status + " finisce la mia visita, ne rimarranno " + --visits);
			reparto.endVisita(this);
			
			try {
				Thread.sleep((long)(Math.random() * 1000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setDoctor(int doctor) {
		this.doctor = doctor;
	}
	
	public int getDoctor() {
		return doctor;
	}
	
	public int getStatus() {
		return status;
	}
}
