package com.fwtai.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//定时(延时)的线程池
public class newScheduledThreadPoolDemo{

    public static void main(String[] args){
        final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);//todo 定时的线程池,支持定时及周期性的任务执行
        System.out.println("开始……");
        /*for(int i = 0; i < 10; i++){
            final int index = i;
            scheduledThreadPool.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println("延迟3秒后执行,i->"+index);
                }
            }, 3,TimeUnit.SECONDS);//延时的单位,延时3秒后才执行
        }*/

        //周期性[定期]执行任务,表示延迟1秒后每3秒执行一次。ScheduledExecutorService比Timer更安全，功能更强大
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("延时5秒后才执行,而后每隔20秒执行一次");
            }
        },5,20,TimeUnit.SECONDS);//延时5秒后才执行,而后每隔20秒执行一次

        //scheduledThreadPool.shutdown();//释放
    }
}