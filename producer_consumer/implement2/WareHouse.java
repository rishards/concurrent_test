package producer_consumer.implement2;

/*�ֿ� ���������Ʒ�Ľ������� ��ο�BlockingQueue��ʵ��
 * ����������ģ����е�ʵ�֣�����֧��ͬʱ��д����ĳ��ʱ��ֻ����һ���������Բֿ���д���
 * �ֿ���Ʒ�Ƚ��ȳ�  ʹ��synchronized ʵ��happens-before 
 * ��֧��ͬʱ��д��ԭ�����ڲ���д����ô���� ��Ҫʹ�õ� ԭ����(�ײ㶼����volatileʵ��)
 * д������ʵ��
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
