package org.example;

import java.util.concurrent.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ExecutionException, InterruptedException {
        System.out.println( "Hello World!" );
        A a =  new A();
        Thread tA = new Thread(a);
        tA.start();

        ExecutorService executorService = Executors.newCachedThreadPool();

        B b = new B();
        // 提交 Callable 任务
        Future<String> future = executorService.submit(b);

        // 获取结果
        String result = future.get();
        System.out.println(result);

        // 关闭线程池
        executorService.shutdown();


    }
}
class A implements Runnable{
    public void run(){
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "线程-" + i);
        }
    }
}

class B implements Callable<String>{
    public String call(){
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "线程-" + i);
        }
        return Thread.currentThread().getName();
    }
}