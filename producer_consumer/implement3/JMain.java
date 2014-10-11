package producer_consumer.implement3;
/*
 * 	使用BlockingQueue完成的生产者/消费者模型
 	Java中BlockingQueue实现原理
  	用 ReentrantLock 实现的BlockingQueue
	双端锁 {putLock takeLock 原子类型count)
	put时，需要得到putLock 除此之外，还需要完整响应的条件，所以需要使用到
	但队列是满的时候，会被阻塞，需要等待，这是最好将线程置于wait()状态
	实现方式是{Condition notFull = putLock.newCondtion} 条件和锁关联起来，等到条件满足为止
	while(队列是满的){
     //这里可以释放条件关联的锁
     notFull.await(); //那么该条件是在如何被notify的呢，在合适位置例如进队列 出队列时发出提示 
                              //notFull.signal(); 在任何改变队列状态的位置发出通知
	}
 * 
 * 
 * */
import java.util.concurrent.LinkedBlockingQueue;

public class JMain {

	public static void main(String[] args) {
		LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
		Producer p = new Producer(queue);
		Consumer c1 = new Consumer(queue);
		Consumer c2 = new Consumer(queue);
		new Thread(p).start();
		new Thread(c1).start();
		new Thread(c2).start();
	}
}
