package assignment3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Autore: Alessandro Puccia
 * Corso A
 * Matricola: 547462
 */
public class Reparto {
	private boolean[] doctors;
	private ReentrantLock repartoLock;
	private Condition red, white;
	private Condition[] yellows;
	
	public Reparto() {
		int i;

		repartoLock = new ReentrantLock(true);
		
		//inizializzo le variabili condizione per la gestione dei pazienti in codice rosso e 
		//bianco.
		red = repartoLock.newCondition();
		white = repartoLock.newCondition();
		
		//inizializzo le risorse e le variabili condizione per la gestione dei pazienti
		//in codice giallo.
		doctors = new boolean[10];
		yellows = new Condition[10];
		for (i = 0; i < 10; i++) {
			doctors[i] = true;
			yellows[i] = repartoLock.newCondition();
		}
	}
	
	public void startVisita(Paziente paziente) {
		repartoLock.lock();
		
		switch(paziente.getStatus()) {
			case 1: {
				//paziente codice rosso aspetta fino a quando tutti i dottori sono liberi.
				while (!checkAllDocs()) {
					try {
						System.out.println("Sono il paziente " + Thread.currentThread().getId() +
								" con codice 1 in attesa");
						red.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//occupo tutti i dottori.
				occupyAllDocs();
				break;
			}
			case 2: {
				//paziente codice giallo aspetta fino a quando non ci sono pazienti in codice rosso
				// oppure non è disponibile il dottore scelto come preferenza.
				
				//ottengo la preferenza scelta dal paziente.
				int doctor = paziente.getDoctor();
				
				while(repartoLock.hasWaiters(red) || !doctors[doctor]) {
					try {
						System.out.println("Sono il paziente " + Thread.currentThread().getId() +
								" con codice 2 in attesa");
						yellows[doctor].await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//occupo il dottore.
				doctors[doctor] = false;
				break;
			}
			case 3: {
				int doctor;
				
				//paziente codice bianco aspetta fino a quando non ci sono pazienti in codice
				//rosso oppure non è disponibile un dottore o se ci sono pazienti gialli in attesa
				//su quel dottore.
				while (repartoLock.hasWaiters(red) || (doctor = getFirstFreeDoc()) == -1 || 
						repartoLock.hasWaiters(yellows[doctor])) {
					try {
						System.out.println("Sono il paziente " + Thread.currentThread().getId() +
								" con codice 3 in attesa");
						white.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//memorizzo il dottore associato al paziente e lo occupo.
				paziente.setDoctor(doctor);
				doctors[doctor] = false;
			}
		}
		
		repartoLock.unlock();
	}
	
	public void endVisita(Paziente paziente) {
		int doctor = paziente.getDoctor();
		
		repartoLock.lock();
		
		switch (paziente.getStatus()) {
			case 1:
				//paziente con codice rosso: se sono presenti altri pazienti con codice rosso li
				//risveglio, altrimenti risveglio tutti i pazienti con codice giallo e bianco.
				
				//libero tutti i dottori.
				freeAllDocs();
				
				if (repartoLock.hasWaiters(red)) {
					red.signal();
				}
				else {
					for (int i = 0; i < 10; i++) {
						yellows[i].signal();
					}
		
					white.signalAll();
				}
				
				break;
			case 2:
			case 3:
				//caso paziente con codice giallo/bianco: risveglio in ordine un rosso, altrimenti
				//un giallo, altrimenti un bianco.
				
				//libero il dottore.
				doctors[doctor] = true;
				
				if (repartoLock.hasWaiters(red)) {
					red.signal();
				}
				else if (repartoLock.hasWaiters(yellows[doctor])) {
					yellows[doctor].signal();
				}
				else {
					white.signal();
				}
		}
		
		repartoLock.unlock();
	}
	
	//funzione che restituisce l'indice del primo dottore libero.
	public int getFirstFreeDoc() {
		int i = 0;
		
		while (i < 10) {
			if (doctors[i] == true) {
				return i;
			}
			i++;
		}
		
		return -1;
	}
	
	//funzione che restituisce true se e solo se tutti i dottori sono liberi.
	public boolean checkAllDocs() {
		int i = 0;
		
		while (i < 10) {
			if (doctors[i] == false) {
				return false;
			}
			i++;
		}
		
		return true;
	}
	
	//funzione che occupa tutti i dottir settandoli a false.
	public void occupyAllDocs() {
		for (int i = 0; i < 10; i++) {
			doctors[i] = false;
		}
	}
	
	//funzione che libera tutti i dottori settandoli a true.
	public void freeAllDocs() {
		for (int i = 0; i < 10; i++) {
			doctors[i] = true;
		}
	}
}
