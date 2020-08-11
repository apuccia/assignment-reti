package assignment4;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// creo il gestore della lista.
		PathQueue pathQueue = new PathQueue();
		
		// creo il produttore.
		String dirPath = args[0];
		Producer producer = new Producer(dirPath, pathQueue);
		Thread producerThread = new Thread(producer);
		
		// creo i consumatori.
		Consumer consumers[] = new Consumer[4];
		Thread consumersThreads[] = new Thread[4];
		
		for (int i = 0; i < 4; i++) {
			consumers[i] = new Consumer(pathQueue);
			consumersThreads[i] = new Thread(consumers[i]);
			consumersThreads[i].start();
		}
		
		producerThread.start();
	}

}
