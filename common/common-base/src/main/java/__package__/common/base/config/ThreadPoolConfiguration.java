package __package__.common.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 覆盖 spring 默认线程池
 *
 * @author Mr_W
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
public class ThreadPoolConfiguration {

	@Value("${spring.application.name}")
	private String applicationName;

	@Bean("commonThreadPool")
	@ConditionalOnMissingBean(name = "commonThreadPool")
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(Runtime.getRuntime().availableProcessors() + 1);
		pool.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2 + 2);
		pool.setQueueCapacity(50);
		pool.setThreadNamePrefix(applicationName + "-");
		return pool;
	}

}
