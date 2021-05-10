package com.fwtai.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//看类名单个线程在执行,即每时每刻只有一个线程在执行
public final class newSingleThreadExecutorDemo{

    public static void main(String[] args) throws InterruptedException{
        final ExecutorService newCachedThreadPool = Executors.newSingleThreadExecutor();
        for(int x = 0; x <10; x++){
            newCachedThreadPool.execute(new Thread(new Pool04(),"线程"+x));
        }
        newCachedThreadPool.shutdown();//释放线程池
    }
}

final class Pool04 implements Runnable{

    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName()+"线程已运行");
        try {
            Thread.sleep(500);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"线程已结束");
    }
}