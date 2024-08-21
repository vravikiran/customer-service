package com.app.service.customer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
/**
 * Contains thread pool configuration to run parallel tasks while validating customer fields retrieved from excel sheet
 */
@Configuration
public class ThreadPoolConfig {
	@Value("${threadpool.core.size}")
	private int threadPoolCoreSize;
	@Value("${threadpool.max.size}")
	private int threadPoolMaxSize;
	/**
	 * creates thread pool for customerFieldsByNameService instances
	 * @return
	 */
	 @Bean(name = "validateCustFieldsByNamesExecutor")
	    public Executor validateCustFieldsByNamesExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(threadPoolCoreSize);
	        executor.setMaxPoolSize(threadPoolMaxSize);
	        executor.setThreadNamePrefix("validateCustFieldsByNamesExecutor-");
	        executor.initialize();
	        return executor;
	    }

	 	/**
	 	 * creates thread pool of CustomerFieldsValidator service instances
	 	 * @return
	 	 */
	    @Bean(name = "taskExecutor")
	    public Executor custFieldsValidatorExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(threadPoolCoreSize);
	        executor.setMaxPoolSize(threadPoolMaxSize);
	        executor.setThreadNamePrefix("validateCustFieldsExecutor-");
	        executor.initialize();
	        return executor;
	    }
}
