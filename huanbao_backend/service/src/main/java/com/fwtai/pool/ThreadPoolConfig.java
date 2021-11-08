package com.fwtai.pool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * spring线程池,用于更新阅读量之类或登录次数
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021-06-17 14:08
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Configuration
@EnableAsync
public class ThreadPoolConfig{

    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor asynServiceExecutor(){
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);//核心线程池数
        executor.setMaxPoolSize(2147483647);//最大线程池
        executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.setKeepAliveSeconds(60);//线程活跃时间(秒)
        executor.setThreadNamePrefix("update");//线程池名称
        executor.setWaitForTasksToCompleteOnShutdown(true);//等待所有任务结束后关闭线程池
        executor.initialize();//初始化线程池
        return executor;
    }
}