package com.fwtai.pool;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Semaphore是一种基于计算的信号量,它可以是一个阀值,基于此,多个线程竞争获取许可信号；
 * 做自己的申请后归还,超过阀值后,线程申请许可信号将会被阻塞!!!,不过呢可以用特性来做构建一些对象池、资源池之类的,如数据库连接池;
 * 景区的公共厕所,比如厕所只有3个蹲坑(3个信号量)
 * 共有10人排队上厕所,先区3个人,此时100%有厕所用;
 * 出来1个人，其他抢占排队进去;
 * join 特殊情况,比如有某个名人要上厕所,此时大家肯定会让着它
 *
*/
public final class SemaphoreDemo{

    public static void main(String[] args){
        final Semaphore wc = new Semaphore(3);//允许的数目
        //new 10 个线程
        for(int i = 0; i < 10; i++){
            final Wc wc1 = new Wc("第" + i + "个人",wc);
            wc1.start();
        }
    }
}

final class Wc extends Thread{

    private String name;
    private Semaphore wc;

    public Wc(final String name,final Semaphore semaphore){
        this.name = name;
        this.wc = semaphore;
    }

    @Override
    public void run(){
        final int available = wc.availablePermits();//获取当前可用的资源数量
        if(available > 0){//说明有可用的资源
            System.out.println(name+",好开心,居然还有蹲位拉便便了!!!");
        }else{
            System.out.println(name+",难过哦,差评,这厕所蹲位太少了");
        }
        try{
            wc.acquire();//todo 申请资源
            System.out.println(name+",哈哈,终于抢到厕所了哦");
            Thread.sleep(new Random().nextInt(1000));//模拟上测试的时间
            System.out.println(name+",嗯,舒服,拉完便便轻松多了呀");
            wc.release();//todo 释放资源
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}