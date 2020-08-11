package assignment4;

import java.io.File;

public class Consumer implements Runnable {
	private PathQueue queue;
	
	public Consumer(PathQueue queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			queue.lockQueue();
			
			// aspetto che venga inserito un nome.
			while (queue.isEmpty()) {
				queue.awaitDirPath();
			}
			
			String dirName = queue.getDirPath();
			File dir = new File(queue.getDirPath());
			
			System.out.printf("Sono il thread %ld, navigo la directory %s:",
					Thread.currentThread().getId(), dirName);
			
			for (File file : dir.listFiles()) {
				if (file.isDirectory()) {
					System.out.printf("Nome della directory: %s", file.getName());
				}
				else {
					System.out.printf("Nome del file: %s", file.getName());
				}
				
				System.out.printf("Dimensione: %ld", file.length());
				System.out.printf("Data ultima modifica: %ld", file.lastModified());
			}
			
			queue.unlockQueue();
		}
	}
}
