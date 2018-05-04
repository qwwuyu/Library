package com.qwwuyu.lib.executor;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 优先级执行器
 */
public class PriorityExecutor {
    private ExecutorService executor;
    private static PriorityExecutor instance;

    private PriorityExecutor() {
        executor = new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>(2, new Comparator<Runnable>() {
            @Override
            public int compare(Runnable o1, Runnable o2) {
                int o1Priority = o1 instanceof PriorityRunnable ? ((PriorityRunnable) o1).priority() : 0;
                int o2Priority = o2 instanceof PriorityRunnable ? ((PriorityRunnable) o2).priority() : 0;
                return o2Priority - o1Priority;
            }
        }));
    }

    public static PriorityExecutor getInstance() {
        if (instance == null) {
            synchronized (PriorityExecutor.class) {
                if (instance == null) {
                    instance = new PriorityExecutor();
                }
            }
        }
        return instance;
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    /**
     * 优先级线程
     */
    public interface PriorityRunnable {
        /** 运行优先级,Runnable默认为0,越高在队列的位子越前(越先执行) */
        int priority();
    }
}
