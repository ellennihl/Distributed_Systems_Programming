package Lab1;

public class Philosophers {
	private static int NUM_PHIL = 5;

	public static void main(String[] args) throws InterruptedException {
		Object[] forks = new Object[NUM_PHIL];
		Philosopher[] phils = new Philosopher[NUM_PHIL];
		// Create forks
		for(int i=0; i<phils.length; i++) {
			forks[i]=new Object();
		}
		// Initialize philosophers with two forks each
		for(int i=0; i<phils.length; i++) {
			phils[i]=new Philosopher(i, forks[i], forks[(i+1) % phils.length]);
		}
		for(Thread p : phils)  {
			p.start();
		}
		// Keep running until the user presses ENTER
		new java.util.Scanner(System.in).nextLine();
		for(Thread p : phils) {
			p.interrupt();
		}
		for(Thread p : phils) {
			p.join();
		}
		System.out.println("All philosophers finished dining.");
	}

	static class Philosopher extends Thread {
		private final Object leftFork, rightFork;
		private final int id;

		public Philosopher(int id, Object leftFork, Object rightFork) {
			this.id = id;
			this.leftFork = leftFork;
			this.rightFork = rightFork;
		}

		private void eat() throws InterruptedException {
			System.out.println("Philosopher "+id+" is eating.");
			Thread.sleep((long)(Math.random()*3));
		}

		private void think() throws InterruptedException {
			System.out.println("Philosopher "+id+" is thinking.");
			Thread.sleep((long)(Math.random()*3));
		}

		private void waiting() throws InterruptedException {
			System.out.println("Philosopher "+id+" is waiting.");
			Thread.sleep((long)(Math.random()*3));
		}
		
		public synchronized void take(Object fork) {
			
		}

		public void run( ) {
			try {
				while(!interrupted()) {
					think();
					waiting();
					
					//if you are person nr 0 then pick upp rightFork first, else take left.
					if(id == 0) {
						synchronized(leftFork) {synchronized(rightFork) {eat();}}
					}
					else {
						synchronized(rightFork) {synchronized(leftFork) {eat();}}
					}
					
				}
			} catch(InterruptedException ie) {
				// Do nothing, this will not happen
			}
		}
	}
}