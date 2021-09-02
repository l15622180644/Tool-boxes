package com.lzk.toolboxes.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author
 * @module
 * @date 2021/6/3 11:42
 */
@Component
public class ScheduledThread {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);

//    @PostConstruct
    public void init() {
//        等待设置的时间后开始周期性执行
        threadPool.schedule(() -> {
            log.info("");
        }, 20, TimeUnit.SECONDS);

//        如果上一个任务的执行时间大于等待时间，任务结束后，下一个任务马上执行
        threadPool.scheduleAtFixedRate(() -> {
            log.info("");
        }, 15, 5, TimeUnit.SECONDS);

//        如果上个任务的执行时间大于等待时间，任务结束后也会等待相应的时间才执行下一个任务
        threadPool.scheduleWithFixedDelay(() -> {
            log.info("");
        }, 17, 5, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void destroy() {
        log.info("开始关闭线程池");
        try {
            threadPool.shutdown();
            if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
                if (threadPool.isTerminated()) {
                    log.info("线程池已关闭");
                }
            } else {
                log.info("线程池已关闭");
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            e.printStackTrace();
        }
    }




}
