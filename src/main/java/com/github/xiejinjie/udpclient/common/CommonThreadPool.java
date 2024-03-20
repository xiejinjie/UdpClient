package com.github.xiejinjie.udpclient.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * CommonThreadPool
 *
 * @author xiejinjie
 * @date 2023/12/25
 */
public class CommonThreadPool {
    public static final ExecutorService threadPool = Executors.newCachedThreadPool();
    public static final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
}
