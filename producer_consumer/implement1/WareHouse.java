package producer_consumer.implement1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*�ֿ� ���������Ʒ�Ľ������� ��ο�BlockingQueue��ʵ��
 * ����������ģ����е�ʵ�֣�����֧��ͬʱ��д����ĳ��ʱ��ֻ����һ���������Բֿ���д���
 * �ֿ���Ʒ�Ƚ��ȳ�  ʹ��ReentratLock ʵ��happens-before 
 * ��֧��ͬʱ��д��ԭ�����ڲ���д����ô���� ��Ҫʹ�õ� ԭ����(�ײ㶼����volatileʵ��)
 * д������ʵ��
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
			//����ڵȴ������б��ж�
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
