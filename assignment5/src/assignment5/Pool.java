package assignment5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * Autore: Alessandro Puccia
 * Corso A
 * Matricola: 547462
 */

public class Pool {
	private final ExecutorService pool;
	
	public Pool() {
		pool = Executors.newFixedThreadPool(4);
	}
	
	public void insertContoCorrente(ConsumerTask contatore) {
		pool.execute(contatore);
	}
	
	public void shutdown() {
		pool.shutdown();
	}
	
	public void awaitShutdown() {
		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
