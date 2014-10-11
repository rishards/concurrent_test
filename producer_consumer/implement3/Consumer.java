package producer_consumer.implement3;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
	private final BlockingQueue<Object> queue;
	
	public Consumer(BlockingQueue<Object> queue) {
		this.queue = queue;
	}
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(2*1000);
				comsume(queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void comsume(Object x) {
		int id = 0;
		if( x instanceof Product){
			id = ((Product)x).getId();
		}
		String str = Thread.currentThread().getName();
		System.out.println(id+" 号产品被 "+str+" 消耗");
	}

}
