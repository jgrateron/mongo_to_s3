package com.nubefact.ose.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadConfig 
{
	@Value("${ose.min_thread}")
	private Integer minThread;

	@Value("${ose.min_thread}")
	private Integer maxThread;
	
	@Bean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(minThread);
        executor.setMaxPoolSize(maxThread);
        executor.setThreadNamePrefix("task-");
        executor.initialize();
        return executor;
    }
}
