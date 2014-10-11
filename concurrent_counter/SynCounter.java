package concurrent_counter;

public class SynCounter {
	private int c = 0;
	public synchronized void increment(){
		c++;
	}
	public synchronized void decrement(){
		c--;
	}
	public synchronized int getValue(){
		return c;
	}
	public synchronized int increamentAndGet(){
		return c++;
	}
	public SynCounter() {
	}
}
