package com.tool.task;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskManager{
    private static TaskManager taskManager;
    private ThreadPoolExecutor threadPool;
    private PriorityBlockingQueue<Runnable> blockingQueue;
    public static TaskManager getInstance(){
        if(taskManager == null){
            taskManager = new TaskManager();
        }
        return  taskManager;
    }

    public void init(int corePoolSize,
                     int maximumPoolSize,
                     long keepAliveTime,
                     TimeUnit unit){
        blockingQueue = new PriorityBlockingQueue<Runnable>();
        threadPool = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,blockingQueue);
        threadPool.allowCoreThreadTimeOut(true);
    }

    public void setCorePoolSize(int corePoolSize){
        if(corePoolSize>threadPool.getMaximumPoolSize()){
            return;
        }else if(corePoolSize == threadPool.getCorePoolSize()){
            return;
        }
        threadPool.setCorePoolSize(corePoolSize);
    }


    public int getMaxThreadCount(){
        return Runtime.getRuntime().availableProcessors() * 3 + 2;
    }
    
    public void addTask(Runnable runnable){
    	threadPool.execute(runnable);
    }
}
