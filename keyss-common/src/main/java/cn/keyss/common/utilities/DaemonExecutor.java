package cn.keyss.common.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 后台任务执行器
 * 通常为定期执行后台任务，当程序关闭时需要优雅退出
 */
public class DaemonExecutor {
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(DaemonExecutor.class);

    /**
     * 线程池执行器
     */
    private ThreadPoolExecutor executor;

    /**
     * 有优先级的关闭程序
     */
    private TreeMap<Integer, ArrayList<Runnable>> shutDownTask;

    /**
     * 同步锁
     */
    private Object syncLocker;

    /**
     * 构造方法
     *
     * @param coreThreadSize 核心线程数
     * @param maxThreadSize  最大线程数
     * @param keepAliveTime  以称为单位的最大存活时间
     * @param queueCapacity  队列长度
     */
    public DaemonExecutor(int coreThreadSize, int maxThreadSize, int keepAliveTime, int queueCapacity) {
        syncLocker = new Object();
        shutDownTask = new TreeMap<>();

        executor = new ThreadPoolExecutor(coreThreadSize, maxThreadSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueCapacity), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName(thread.getName() + "for DaemonExecutor");
                thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        logger.error("Daemon线程执行过程中发生异常", e);
                    }
                });
                return thread;
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (shutDownTask.size()>0) {
                    for (Map.Entry<Integer, ArrayList<Runnable>> entry : shutDownTask.entrySet()) {
                        if(entry.getValue()!=null){
                            for (Runnable running :entry.getValue()){
                                if (running!=null){
                                    running.run();
                                }
                            }
                        }
                    }
                }
            }
        }));
    }

    /**
     * 注册任务
     *
     * @param runnable 任务脚本
     */
    public void registTask(Runnable runnable) {
        this.executor.execute(runnable);
    }

    /**
     * 注册系统退出时任务
     *
     * @param priority 优先级，数字越小，优先级越高
     * @param runnable 任务脚本
     */
    public void registShutdownTask(int priority, Runnable runnable) {
        synchronized (syncLocker) {
            if (shutDownTask.containsKey(priority)) {
                shutDownTask.get(priority).add(runnable);
            } else {
                ArrayList list = new ArrayList();
                list.add(runnable);
                shutDownTask.put(priority, list);
            }
        }
    }
}
