package producer_consumer.implement2;

/*仓库 用来管理产品的进出管理 请参考BlockingQueue的实现
 * 这里用数组模拟队列的实现，但不支持同时读写，即某个时刻只能有一个处理器对仓库进行处理
 * 仓库物品先进先出  使用synchronized 实现happens-before 
 * 不支持同时读写的原因在于不想写到那么复杂 需要使用到 原子类(底层都是用volatile实现)
 * 写出泛型实现
 * */
public  class  WareHouse<AnyType>{
	private AnyType[] products;
	public static final int DEFAULT_CAPACITY = 10;
	private int capacity = 0;
	private int last = 0;
	private int total = 0;
	private boolean debug = false;
	public WareHouse() {
		this(DEFAULT_CAPACITY);
	}
	
	@SuppressWarnings("unchecked")
	public WareHouse(int capacity){
		this.capacity = capacity;
		this.products = (AnyType[]) new Object[capacity];
	}
	
	public synchronized void put(AnyType x) throws InterruptedException{
		if(x == null) throw new NullPointerException();

		try{
			while(total == capacity){
				wait();
			}
			if(debug){
				System.out.println("total:"+total+" "+"capacity:"+capacity);
			}
			enqueue(x);	
		}catch(Exception e){
			e.printStackTrace();
		}
		notify();
	}
	
	public synchronized AnyType take() throws InterruptedException{
		AnyType x = null;
		try{
			while(total == 0){
				wait();
			}
			x = dequeue();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		notify();
		return x;
	}
	
	private void enqueue(AnyType x){
		if(isFull()){
			throw new IndexOutOfBoundsException();
		}
		if(debug){
			System.out.println("total:"+total+" last:"+last);
		}
		products[increament(last+total-1)]=x;
		total++;
	}
	
	private AnyType dequeue(){
		if(isEmpty()){throw new NullPointerException();}
		AnyType x = products[last];
		products[last] = null;
		last = increament(last);
		total--;
		return x;
	}
	
	private int increament(int x){
		return (++x)%capacity;
	}
	boolean isEmpty(){
		return total == 0;
	}
	
	boolean isFull(){
		return total == capacity;
	}
}
