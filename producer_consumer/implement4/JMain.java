package producer_consumer.implement4;

public class JMain {
	public static void main(String[] args) {
		WareHouse<Object> wareHouse = new WareHouse<Object>();
		Producer p = new Producer(wareHouse);
		Consumer c1 = new Consumer(wareHouse);
		Consumer c2 = new Consumer(wareHouse);
		new Thread(p).start();
		new Thread(c1).start();
		new Thread(c2).start();
	}
}
