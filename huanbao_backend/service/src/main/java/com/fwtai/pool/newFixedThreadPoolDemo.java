package com.fwtai.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//固定长度的线程池,就是线程池里面只能放多少个线程,即定5个的线程池,若有再多想线程调用时会阻塞
public final class newFixedThreadPoolDemo{

    public static void main(String[] args) throws InterruptedException{
        final ExecutorService newCachedThreadPool = Executors.newFixedThreadPool(5);
        for(int x = 0; x <20; x++){
            Thread.sleep(500);
            newCachedThreadPool.execute(new Thread(new Pool02(),"线程"+x));
        }
        newCachedThreadPool.shutdown();//释放线程池
    }
}

final class Pool02 implements Runnable{

    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName()+"线程已运行");
        try {
            Thread.sleep(1000);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"线程已结束");
    }
}