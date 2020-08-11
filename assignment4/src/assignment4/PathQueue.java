package assignment4;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PathQueue {
	private List<String> queue;
	private Lock queueLock;
	private Condition queueEmpty;
	
	private Consumer[] consumers;
	private Thread[] consumersThreads;
	
	public PathQueue() {
		this.queue = new LinkedList<String>();
		queueLock = new ReentrantLock();
		queueEmpty = queueLock.newCondition();
		
	}
	
	public void lockQueue() {
		queueLock.lock();
	}
	
	public void unlockQueue() {
		queueLock.unlock();
	}
	
	public void addDirPath(String path) {
		queue.add(path);
	}
	
	public String getDirPath() {
		String path;
		
		path = queue.get(0);
		
		return path;
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	public void awaitDirPath() {
		try {
			queueEmpty.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void notifyDirPath() {
		queueEmpty.notify();
	}
}
