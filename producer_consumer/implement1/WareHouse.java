package producer_consumer.implement1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*仓库 用来管理产品的进出管理 请参考BlockingQueue的实现
 * 这里用数组模拟队列的实现，但不支持同时读写，即某个时刻只能有一个处理器对仓库进行处理
 * 仓库物品先进先出  使用ReentratLock 实现happens-before 
 * 不支持同时读写的原因在于不想写到那么复杂 需要使用到 原子类(底层都是用volatile实现)
 * 写出泛型实现
 * */
public  class  WareHouse<AnyType>{
	private AnyType[] products;
	public static final int DEFAULT_CAPACITY = 10;
	private int capacity = 0;
	private int last = 0;
	private int total = 0;
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition notFull = lock.newCondition();
	private final Condition notEmpty = lock.newCondition();
	
	
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
		final ReentrantLock lock = this.lock;
		lock.lockInterruptibly();
		try{
			while(total == capacity){
				notFull.await();
			}
			enqueue(x);	
			if(total < capacity){
				notFull.signal();
			}
		}finally{
			//如果在等待过程中被中断
			lock.unlock();
		}
		if(!isEmpty()){
			signalNotEmpty();
		}
	}
	
	public AnyType take() throws InterruptedException{
		AnyType x;
		final ReentrantLock lock = this.lock;
		lock.lock();
		try{
			while(total == 0){
				notEmpty.await();
			}
			x = dequeue();
			if(!isEmpty()){
				notEmpty.signal();
			}
		}finally{
			lock.unlock();
		}
		if(!isFull())
			signalNotFull();
		return x;
	}
	
	private void signalNotEmpty(){
		final ReentrantLock lock = this.lock;
		lock.lock();
		try{
			notEmpty.signal();
		}finally{
			lock.unlock();
		}
	}
	
	private void signalNotFull(){
		final ReentrantLock lock = this.lock;
		lock.lock();
		try{
			notFull.signal();
		}finally{
			lock.unlock();
		}
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
