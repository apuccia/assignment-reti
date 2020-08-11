package assignment1;

public class PICalculator implements Runnable {
	private double accuracy;
	
	public PICalculator(double accuracy) {
		this.accuracy = accuracy;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		double PI = 4, den = 3, sign = -1;
		
		while(!Thread.interrupted() && (Math.abs((PI - Math.PI)) >= accuracy)) {
			PI += sign * (4/den);
			den += 2;
			sign = -sign;
		}
		
		if(!Thread.interrupted()) {
			System.out.println("Calcolo terminato: " + PI);
		}
	}
}
