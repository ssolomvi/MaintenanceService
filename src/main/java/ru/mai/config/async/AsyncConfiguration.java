package ru.mai.config.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mai.config.property.ExecutorConfiguration;

import java.util.concurrent.*;

@Configuration
public class AsyncConfiguration {

    @Autowired
    private ExecutorConfiguration executorConfiguration;

    @Bean(name = "workshopScheduledExecutor")
    public ScheduledExecutorService workshopScheduledExecutor() {
        var config = executorConfiguration.getWorkshopScheduled();

        var scheduledExecutor = new ScheduledThreadPoolExecutor(config.getCorePoolSize());
        scheduledExecutor.setMaximumPoolSize(config.getMaxPoolSize());
        scheduledExecutor.setThreadFactory(new NamingThreadFactory("workshopScheduledExecutor-"));

        return scheduledExecutor;
    }

    @Bean(name = "repairingScheduledExecutor")
    public ScheduledExecutorService repairingScheduledExecutor() {
        var config = executorConfiguration.getRepairingServiceScheduled();

        var scheduledExecutor = new ScheduledThreadPoolExecutor(config.getCorePoolSize());
        scheduledExecutor.setMaximumPoolSize(config.getMaxPoolSize());
        scheduledExecutor.setThreadFactory(new NamingThreadFactory("repairingScheduledExecutor-"));

        return scheduledExecutor;
    }

    @Bean(name = "repairingExecutor")
    public ExecutorService repairingExecutor() {
        var config = executorConfiguration.getRepairingService();

        return new ThreadPoolExecutor(
                config.getCorePoolSize(),
                config.getMaxPoolSize(),
                config.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(config.getQueueCapacity()),
                new NamingThreadFactory("repairingExecutor-"),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

}
