package assignment4;

import java.io.File;


public class Producer implements Runnable {
	private PathQueue queue;
	private File mainDir;
	
	public Producer(String dirPath, PathQueue queue) {
		this.queue = queue;
		
		mainDir = new File(dirPath);
	}

	public void produceDirPath(String path) {
		//aggiungo il path e notifico un consumatore dell'aggiunta.
		queue.lockQueue();
		
		queue.addDirPath(mainDir.getName());
		queue.notifyDirPath();
		
		queue.unlockQueue();
	}
	
	public void walkDir(File mainDir) {

		for (File file : mainDir.listFiles()) {
			if (file.isDirectory()) {
				// se il file è una directory lo aggiungo alla lista e ricorro.
				produceDirPath(file.getPath());

				walkDir(file);
			}
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		// path passato non corrisponde ad una directory.
		if (!mainDir.isDirectory()) {
			return;
		}
		
		// inserisco il nome della cartella principale.
		produceDirPath(mainDir.getName());
		
		// navigo ricorsivamente.
		walkDir(mainDir);
	}
}
