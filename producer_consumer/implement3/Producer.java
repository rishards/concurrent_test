package producer_consumer.implement3;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
	private final BlockingQueue<Object> queue;
	private int product_id = 0;
	public Producer(BlockingQueue<Object> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(1000*1);
				queue.put(produce());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	Object produce(){
		product_id++;
		String str = new String("产品"+product_id+"号生产完成！");
		System.out.println(str);
		Product p = new Product(product_id);
		return p;
	}
	

}
