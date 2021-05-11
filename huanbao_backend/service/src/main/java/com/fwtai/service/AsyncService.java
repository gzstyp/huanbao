package com.fwtai.service;

import com.fwtai.core.AsyncDao;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步处理,注意，这里的异步方法，只能在本类的自身之外调用，在本类调用是无效的!!!,即在别的类调用即可
 * @注意 需要在启动类添加开启异步处理@EnableAsync注解类
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-10 20:11
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Component
public class AsyncService{

    @Resource
    private AsyncDao asyncDao;

    // 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程,
    // 但是这方式会使线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程。todo 就是'会把之前执行完毕的线程不释放,会留在线程池中给下一个调用的线程直接调用'
    @Async
    public void updateLogin(final String username){
        final ExecutorService threadPool = Executors.newCachedThreadPool();//todo 看类名就是缓存的线程池,它是不固定的,可以放很多个线程
        threadPool.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    asyncDao.updateSucceed(username);
                } catch (final Exception ignored){
                }finally{
                    threadPool.shutdown();//释放线程池
                }
            }
        });
    }

    /*
        创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
        因为线程池大小为3，每个任务输出index后sleep 2秒，所以每两秒打印3个数字。
        定长线程池的大小最好根据系统资源进行设置。如Runtime.getRuntime().availableProcessors()。可参考PreloadDataCache。
     */
    protected void executeNewFixedThreadPool(){
        final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);//todo 固定长度的线程池,就是线程池里面只能放多少个线程,即定3个的线程池,若有再多想线程调用时会阻塞
        for (int i = 0; i < 10; i++) {
            final int index = i;
            fixedThreadPool.execute(new Runnable(){
                @Override
                public void run() {
                    try {
                        System.out.println(index);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /*
        创建一个定长线程池，支持定时及周期性[定期执行]任务执行,表示延迟3秒执行。
    */
    protected void executeNewScheduledThreadPool(){
        //定时执行
        final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);//todo 看类名就知道定时的线程池,支持定时及周期性的任务执行
        scheduledThreadPool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("延迟3秒后才执行");
            }
        }, 3,TimeUnit.SECONDS);//延时的单位,延时3秒后才执行

        //周期性[定期]执行任务,表示延迟1秒后每3秒执行一次。ScheduledExecutorService比Timer更安全，功能更强大
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("表示延时5秒后才执行,而后每隔10秒执行一次");
            }
        },5,10,TimeUnit.SECONDS);//延时5秒后才执行,而后每隔10秒执行一次

        //scheduledThreadPool.shutdown();//释放
    }

    /*
        创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
    */
    protected void executeNewSingleThreadExecutor(){
        final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();//todo 看类名就是单个线程在执行,即每时每刻只有一个线程在执行
        for (int i = 0; i < 10; i++) {
            final int index = i;
            singleThreadExecutor.execute(new Runnable(){
                @Override
                public void run() {
                    try {
                        System.out.println(index);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}