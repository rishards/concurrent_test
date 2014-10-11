package producer_consumer.implement1;

public class Producer implements Runnable {
	private final WareHouse<Object> wareHouse;
	private int product_id = 0;
	public Producer(WareHouse<Object> wareHouse) {
		this.wareHouse = wareHouse;
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(500);
				wareHouse.put(produce());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	Product produce(){
		product_id++;
		String str = new String("产品"+product_id+"号生产完成！");
		System.out.println(str);

		Product p = new Product(product_id);
		return p;
	}
	

}
