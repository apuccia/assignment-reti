package assignment2;

/*
 * Autore: Alessandro Puccia corso A
 * Matricola: 547462
 */

public class TaskSpawner implements Runnable {
	private PostOffice postOffice;
	
	public TaskSpawner(PostOffice postOffice) {
		this.postOffice = postOffice;
	}

	public void run() {
		// TODO Auto-generated method stub
		
		int i = 0;
		long time = 0;
		
		while (!Thread.interrupted()) {
			Person person = new Person(i);
			
			if (!postOffice.insert(person)) {
				// Caso in cui non c'è spazio nella prima coda.
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
	
			}
			time += 50;
			
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Thread.currentThread().interrupt();
				continue;
			}
			
			i++;
		}
	}
}
