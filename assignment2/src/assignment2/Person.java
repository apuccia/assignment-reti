package assignment2;

/*
 * Autore: Alessandro Puccia corso A
 * Matricola: 547462
 */

public class Person implements Runnable {
	private int identifier;
	
	public Person(int identifier) {
		this.identifier = identifier;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("[" + java.time.LocalTime.now() + "] Sono il numero " + identifier +
				" e sono arrivato allo sportello " + Thread.currentThread().getName() + 
				", inizio l'operazione.");
		
		try {
			Thread.sleep((long)(Math.random() * 10000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("[" + java.time.LocalTime.now() + "] Sono il numero " + identifier +
				" e ho finito l'operazione, esco dallo sportello " +
				Thread.currentThread().getName() + ".");
	}

	public void enterFirstQueue() {
		System.out.println("[" + java.time.LocalTime.now() + "] Sono il numero " + identifier + 
				" e sono entrato nella prima coda.");
	}
	
	public void exitFirstQueue() {
		System.out.println("[" + java.time.LocalTime.now() + "] Sono il numero " + identifier +
				" e sono uscito dalla prima coda.");
	}
	
	public void enterSecondQueue() {
		System.out.println("[" + java.time.LocalTime.now() + "] Sono il numero " + identifier +
				" e sono entrato nella seconda coda.");
	}
	
	public void exitSecondQueue() {
		System.out.println("[" + java.time.LocalTime.now() + "] Sono il numero " + identifier +
				" e sono uscito dalla seconda coda.");
	}
}
