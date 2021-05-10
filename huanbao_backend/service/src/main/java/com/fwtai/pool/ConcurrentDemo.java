package com.fwtai.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class ConcurrentDemo{

    public static void main(final String[] args) throws InterruptedException{
        final BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);//10个大小;
        final Producer p1 = new Producer(queue);
        final Producer p2 = new Producer(queue);

        final Consumer consumer = new Consumer(queue);

        final Thread producer1 = new Thread(p1);
        final Thread producer2 = new Thread(p2);
        new Thread(consumer).start();

        producer1.start();
        producer2.start();

        Thread.sleep(10 * 1000);

        producer1.stop();
        producer2.stop();
    }
}

//创建一个生产者线程
final class Producer implements Runnable{

    private final BlockingQueue<String> queue;

    public volatile boolean flag = true;

    public AtomicInteger num = new AtomicInteger(0);

    public Producer(final BlockingQueue<String> queue){
        this.queue = queue;
    }

    @Override
    public void run(){
        System.err.println("生产者线程开始执行了");
        while(flag){
            System.out.println("正在生产数据");
            final String value = String.valueOf(num.incrementAndGet());
            try{
                final boolean offer = queue.offer(value,2,TimeUnit.SECONDS);//todo 这里是阻塞2秒去添加数据,意思是可以在这里用2秒钟的时间去添加!
                if(offer){
                    System.out.println("生产者,存入数据"+value+"到队列中,成功!");
                }else{
                    System.out.println("生产者,存入数据"+value+"到队列中,失败!");
                }
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }finally{
                System.out.println("生产者,退出线程");
            }
        }
    }

    public void stop(){
        this.flag = false;
    }
}

//创建一个消费者线程
final class Consumer implements Runnable{

    private final BlockingQueue<String> queue;

    public volatile boolean flag = true;

    public Consumer(final BlockingQueue<String> queue){
        this.queue = queue;
    }

    @Override
    public void run(){
        System.out.println("消费者线程已启动");
        try {
            while(flag){
                System.out.println("消费者,正在从队列中获取数据……");
                final String value = queue.poll(2,TimeUnit.SECONDS);//todo 这里是阻塞2秒去获取数据,意思是可以在这里用2秒钟的时间去获取!
                if(value != null){
                    System.out.println("消费者从队列中拿到了数据"+value);
                    Thread.sleep(1000);
                }else{
                    System.out.println("消费者超过2秒钟未获取到数据");
                    flag = false;
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }finally{
            System.out.println("消费者,退出线程");
        }
    }

    public void stop(){
        this.flag = false;
    }
}