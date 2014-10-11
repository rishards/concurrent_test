package producer_consumer.implement4;

import java.util.concurrent.Semaphore;

/*仓库 用来管理产品的进出管理 请参考BlockingQueue的实现
 * 这里用数组模拟队列的实现，但不支持同时读写，即某个时刻只能有一个处理器对仓库进行处理
 * 仓库物品先进先出  使用Semaphore 实现happens-before 
 * 不支持同时读写的原因在于不想写到那么复杂 需要使用到 原子类(底层都是用volatile实现)
 * 写出泛型实现
 * */
public  class  WareHouse<AnyType>{
	private AnyType[] products;
	public static final int DEFAULT_CAPACITY = 10;
	private int capacity = 0;
	private int last = 0;
	private int total = 0;
	Semaphore mutex = new Semaphore(1);
	Semaphore notFull = new Semaphore(10);
	Semaphore notEmpty = new Semaphore(0);
	
	
	public WareHouse() {
		this(DEFAULT_CAPACITY);
	}
	
	@SuppressWarnings("unchecked")
	public WareHouse(int capacity){
		this.capacity = capacity;
		this.products = (AnyType[]) new Object[capacity];
	}
	
	public void put(AnyType x) throws InterruptedException{
		if(x == null) throw new NullPointerException();

		if(!isFull()){
			mutex.acquire();
		}
		while(isFull()){
			notFull.acquire();
		}
		enqueue(x);
		if(!isFull()){
			notFull.release();
		}
		notEmpty.release();
		mutex.release();
		
	}
	
	public AnyType take() throws InterruptedException{
		AnyType x;
		
		if(!isEmpty()){
			mutex.acquire();
		}
		while(isEmpty()){
			notEmpty.acquire();
		}
		x = dequeue();
		if(!isEmpty()){
			notEmpty.release();
		}
		notFull.release();
		mutex.release();
		return x;
	}
	
	private void enqueue(AnyType x){
		if(isFull()){
			throw new IndexOutOfBoundsException();
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
		return (++x)%capacity ;
	}
	boolean isEmpty(){
		return total == 0;
	}
	
	boolean isFull(){
		return total == capacity;
	}
}
