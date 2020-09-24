package assignment2;

/*
 * Autore: Alessandro Puccia corso A
 * Matricola: 547462
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PostOffice implements Runnable {
	private BlockingQueue<Person> firstQueue;
	private BlockingQueue<Runnable> secondQueue;
	private ThreadPoolExecutor executor;
	private int groupSize;
	
	public PostOffice(int capacity, int groupSize) {
		this.groupSize = groupSize;
		
		// Alloco la prima e la seconda coda che rappresentano le due stanze.
		firstQueue = (BlockingQueue<Person>) new ArrayBlockingQueue<Person>(capacity);
		secondQueue = (BlockingQueue<Runnable>) new ArrayBlockingQueue<Runnable>(groupSize);
		
		executor = new ThreadPoolExecutor(4, 4, 5, TimeUnit.SECONDS, secondQueue);
		// Mi permette di far terminare i thread del core, in questo modo simulo la chiusura
		// dello sportello.
		executor.allowCoreThreadTimeOut(true); 
		// In questo modo tutti i thread del core sono creati sin dall'inizio.
		executor.prestartAllCoreThreads();
	}
	
	
	// Metodo utilizzato dal task spawner per inserire i task nella prima coda.
	public boolean insert(Person person) {
		if (firstQueue.offer(person)) {
			person.enterFirstQueue();
			return true;
		}
		else {
			return false;
		}
	}
	
	private Person pop() {
		Person person = (Person) firstQueue.poll();
		
		if (person != null) {
			person.exitFirstQueue();
			return person;
		}
		else {
			return null;
		}
	}
	
	private void executeN(int leftFirstQueue) {
		while (leftFirstQueue > 0) {
			Person person = pop();
			
			person.enterSecondQueue();
			executor.execute(person);
							
			leftFirstQueue--;
		}
	}

	public void run() {
		// TODO Auto-generated method stub
		int leftFirstQueue = 0;
		
		while (!Thread.interrupted()) {

			leftFirstQueue = firstQueue.size();
			
			if(executor.getPoolSize() != 4) {
				System.out.println("Uno sportello è stato chiuso per mancanza di richieste.");
			}
			
			if (secondQueue.size() == 0 && leftFirstQueue >= groupSize) {
				// Inserisco soltanto se ci sono almeno groupSize task, in questo modo simulo
				// l'entrata a gruppi di k persone.
				executeN(groupSize);
			}
			else {
				// Non ci sono abbastanza task o non c'è spazio nella seconda stanza.
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					leftFirstQueue = firstQueue.size();
				}
			}
		}
		
		// Sono stato interrotto per la terminazione ma ci possono essere ancora task nella
		// prima coda. In questo caso devo servire le persone che sono ancora nella prima stanza.
		while (leftFirstQueue > 0) {
			int secondQueueFree = secondQueue.remainingCapacity();
			if (secondQueueFree == groupSize) {
				if (leftFirstQueue >= groupSize) {
					// Caso in cui il numero di persone ancora da servire è maggiore di k.
					// In questo caso li faccio entrare comunque a gruppi di k.
					executeN(groupSize);
					leftFirstQueue -= groupSize;
				}
				else if (leftFirstQueue < groupSize) {
					// Caso in cui il numero di persone ancora da servire è minore di k.
					executeN(leftFirstQueue);
					leftFirstQueue -= leftFirstQueue;
				}
			}
			else {
				// Non c'è spazio nella seconda stanza.
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// Aspetto la terminazione dei task rimanenti nella seconda coda.
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
