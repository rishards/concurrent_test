package producer_consumer.implement4;

import java.util.concurrent.Semaphore;

/*�ֿ� ���������Ʒ�Ľ������� ��ο�BlockingQueue��ʵ��
 * ����������ģ����е�ʵ�֣�����֧��ͬʱ��д����ĳ��ʱ��ֻ����һ���������Բֿ���д���
 * �ֿ���Ʒ�Ƚ��ȳ�  ʹ��Semaphore ʵ��happens-before 
 * ��֧��ͬʱ��д��ԭ�����ڲ���д����ô���� ��Ҫʹ�õ� ԭ����(�ײ㶼����volatileʵ��)
 * д������ʵ��
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
