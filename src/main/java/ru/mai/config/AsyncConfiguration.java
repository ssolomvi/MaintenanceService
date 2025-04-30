package ru.mai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
public class AsyncConfiguration {

    @Autowired
    private ExecutorConfiguration executorConfiguration;

    @Bean(name = "workshopExecutor")
    public ScheduledExecutorService workshopExecutor() {
        var config = executorConfiguration.getWorkshop();

        var scheduledExecutor = new ScheduledThreadPoolExecutor(config.getCorePoolSize());
        scheduledExecutor.setMaximumPoolSize(config.getMaxPoolSize());
        scheduledExecutor.setThreadFactory(new SchedulerThreadFactory("workshopExecutor-"));

        return scheduledExecutor;
    }

}
