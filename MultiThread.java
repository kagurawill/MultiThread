package com.wzy;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: create by wzy
 * @description: com.wzy
 * 利用线程池实现多线程，队列存储任务--->
 * 线程池负责管理工作线程，包含一个等待执行的任务队列。
 * 线程池的任务队列是一个Runnable集合，工作线程负责从队列中取出并执行Runnable对象。
 * @date:2019/6/14
 * 该模型的对象有：生产者、消费者、队列（生产者put、消费者take）
 *          ThreadPoolExecutor执行生产消费任务
 */
public class MultiThread {
    private static ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5000);
    private static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5000),new ThreadPoolExecutor.CallerRunsPolicy());
    /**主线程
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i <1000 ; i++) {
            poolExecutor.execute(new ProductThread(i, queue));//执行生产任务
            poolExecutor.execute(new ConsumerThread(queue));
        }
        poolExecutor.shutdown();
    }
}

/**
 * 生产者的线程，它有一个队列来存储生产出来的任务
 */
class ProductThread extends Thread {
    private int taskNum;
    private ArrayBlockingQueue queue;

    public ProductThread(int taskNum, ArrayBlockingQueue queue) {
        this.taskNum = taskNum;
        this.queue = queue;
    }
    public void run(){
        //模拟生产
        try {
//            Thread.currentThread().sleep(5000);
            System.out.println("starting produce"+taskNum);
            queue.put(taskNum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ConsumerThread extends Thread {
   private ArrayBlockingQueue queue;

    public ConsumerThread(ArrayBlockingQueue queue) {
        this.queue = queue;
    }

    public void run(){
        System.out.println("starting consume");
        int taskNum;
        try {
            if (queue.size() > 0) {
                taskNum = (int)queue.take();
                System.out.println("consumed:"+taskNum);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
