package com.fwtai.pool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

//并发队列;
/* todo
    ConcurrentLinkedQueue 是一个适用于高并发场景下的队列,
    它是通过无锁的方式实现了高并发状态下的高性能;
    通常 ConcurrentLinkedQueue 性能好于 BlockingQueue[这个名字一看就是阻塞的],它是一个基于链接点的无界线程安全队列,该队列的元素遵循先进先出的原则;
    头是最先进的,尾是最后进的,该队列不允许有null元素
    它有重要的方法
        add() 和 affer() 都是加入的方法,其中ConcurrentLinkedQueue的这两个方法没有区别
        poll() 和 peek()都是取头元素节点,区别在于前者会删除元素,后者不会!!!
    BlockingQueue 一般用于生产者和消费者的场景
 */
public final class ConcurrentLinkedQueueDemo{

    public static void main(final String[] args){
        final ConcurrentLinkedQueue<String> q = new ConcurrentLinkedQueue<String>();
        q.add("田卓智");
        final String peek1 = q.peek();//不会删除元素
        System.out.println("peek1 = " + peek1);

        final String peek2 = q.peek();
        System.out.println("peek2 = " + peek2);

        final String poll1 = q.poll();//它会删除元素
        System.out.println("poll1 = " + poll1);

        final String poll2 = q.poll();
        System.out.println("poll2 = " + poll2);

        blockQueue();
    }

    //适用于生产者和消费者
    protected static void blockQueue(){
        final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(3);
        queue.add("张山");
        queue.add("李四");
        queue.add("王五");
        queue.poll();//消费,它会删除元素
        queue.add("赵七");//报错,Queue full
        final int size = queue.size();
        System.out.println(size);
    }
}