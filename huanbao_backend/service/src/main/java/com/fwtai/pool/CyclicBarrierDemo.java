package com.fwtai.pool;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//闭锁,计算器,执行减减的操作;todo 当 new CyclicBarrier(3);3个时必须满足3个线程才能执行后面的代码,若只传了2个会一直等待!
public final class CyclicBarrierDemo{

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException{
        //线程的个数,满足了3个，就执行后面代码,所以要严格定义进来的线程数,所以要new 3个线程出来且都执行完毕才执行后面的代码,否则一直等待出现死锁;多运行几次就可以看到效果了
        final CyclicBarrier barrier = new CyclicBarrier(3);//todo,注意和CountDownLatch区别,即一开始的是3个线程数,必须等待有3个线程数才能执行后面的代码
        new ThreadDemo2(barrier).start();
        new ThreadDemo2(barrier).start();
        new ThreadDemo2(barrier).start();
        System.out.println("主线程执行结束");
    }
}

final class ThreadDemo2 extends Thread{

    private final CyclicBarrier cyclicBarrier;

    public ThreadDemo2(final CyclicBarrier cyclicBarrier){
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run(){
        System.out.println(getName()+"开始执行线程");
        try{
            cyclicBarrier.await();
        }catch(InterruptedException | BrokenBarrierException e){
            e.printStackTrace();
        }
        try{
            sleep(2000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        try{
            //第1次进来有1个线程等等,还不满足3个,不释放
            //第2次进来有1个线程等等,不满足3个,不释放
            //第3次进来有1个线程满足了3个，就执行后面代码,所以要严格定义进来的线程数,否则出现死锁
            cyclicBarrier.await();
        }catch(InterruptedException | BrokenBarrierException e){
            e.printStackTrace();
        }
        System.out.println(getName()+"结束执行线程");

    }
}