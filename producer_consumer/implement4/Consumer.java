package producer_consumer.implement4;

public class Consumer implements Runnable{
	private final WareHouse<Object> wareHouse;
	
	public Consumer(WareHouse<Object> wareHouse) {
		this.wareHouse = wareHouse;
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(1000*2);
				consume(wareHouse.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void consume(Object x) {
		int id = 0;
		if( x instanceof Product){
			id = ((Product)x).getId();
		}
		String str = Thread.currentThread().getName();
		System.out.println(id+" 号产品被 "+str+" 消耗");
	}
}
