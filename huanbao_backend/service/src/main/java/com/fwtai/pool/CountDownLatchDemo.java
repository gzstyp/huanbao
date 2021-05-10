package com.fwtai.pool;

import java.util.concurrent.CountDownLatch;

//回环栅栏,执行加加的操作;todo 来一个线程减去一个减少一个线程,直到减完减到0才执行后面的代码,否则线程一直都在等待，注意和CyclicBarrier区分
public final class CountDownLatchDemo{

    public static void main(final String[] args) throws InterruptedException{
        final CountDownLatch c = new CountDownLatch(2);//线程数目,线程计算器,最终从2减到0才执行后面的代码
        ThreadDemo t1 = new ThreadDemo(c);
        ThreadDemo t2 = new ThreadDemo(c);
        t1.start();
        t2.start();
        Thread.sleep(300);
        c.await();
        System.out.println("主线程结束");//zookeeper实现分布式锁采用的就是这个
    }
}

final class ThreadDemo extends Thread{

    private final CountDownLatch countDownLatch;

    public ThreadDemo(final CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run(){
        try {
            sleep(500);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        System.out.println(getName()+"线程执行完毕");
        countDownLatch.countDown();//本线程结束,线程释放,执行的是减减操作!!!每创建一个就结束一个
    }
}