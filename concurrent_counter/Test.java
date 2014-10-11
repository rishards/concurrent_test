package concurrent_counter;

class CounterThread implements Runnable{
	SynCounter counter;
	public CounterThread(SynCounter counter) {
		this.counter = counter;
	}
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+": "+counter.increamentAndGet());
		}
	}
	
}

public class Test {

	public static void main(String[] args) {
		SynCounter counter = new SynCounter();
		CounterThread t = new CounterThread(counter);
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
	}
}
