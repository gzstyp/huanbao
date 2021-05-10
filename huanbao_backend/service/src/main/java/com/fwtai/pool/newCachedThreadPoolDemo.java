package com.fwtai.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//看类名缓存的线程池,它是不固定的,可以放很多个线程,会把之前执行完毕的线程不释放,会留在线程池中给下一个调用的线程直接调用
public final class newCachedThreadPoolDemo{

    public static void main(String[] args) throws InterruptedException{
        final ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        for(int x = 0; x <20; x++){
            Thread.sleep(500);
            newCachedThreadPool.execute(new Thread(new Pool01(),"线程"+x));
        }
        newCachedThreadPool.shutdown();//释放线程池
    }
}

final class Pool01 implements Runnable{

    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName()+"线程已运行");
        try {
            Thread.sleep(1500);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"线程已结束");
    }
}