package producer_consumer.implement3;
/*
 * 	ʹ��BlockingQueue��ɵ�������/������ģ��
 	Java��BlockingQueueʵ��ԭ��
  	�� ReentrantLock ʵ�ֵ�BlockingQueue
	˫���� {putLock takeLock ԭ������count)
	putʱ����Ҫ�õ�putLock ����֮�⣬����Ҫ������Ӧ��������������Ҫʹ�õ�
	������������ʱ�򣬻ᱻ��������Ҫ�ȴ���������ý��߳�����wait()״̬
	ʵ�ַ�ʽ��{Condition notFull = putLock.newCondtion} �������������������ȵ���������Ϊֹ
	while(����������){
     //��������ͷ�������������
     notFull.await(); //��ô������������α�notify���أ��ں���λ����������� ������ʱ������ʾ 
                              //notFull.signal(); ���κθı����״̬��λ�÷���֪ͨ
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
