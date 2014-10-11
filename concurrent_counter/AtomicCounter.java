package concurrent_counter;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter {
	private AtomicInteger c = new AtomicInteger(0);
	public AtomicCounter() {
		// TODO Auto-generated constructor stub
	}
	public void increment(){
		c.incrementAndGet();
	}
	public void decrement(){
		c.decrementAndGet();
	}
	public int value(){
		return c.get();
	}
	public static void main(String[] args) {
		int r = ThreadLocalRandom.current().nextInt(4,77);
		System.out.println(r);
	}
}
