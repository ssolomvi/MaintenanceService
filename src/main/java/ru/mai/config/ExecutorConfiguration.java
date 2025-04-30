package ru.mai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.executor")
public class ExecutorConfiguration {

    private int initialDelay;

    private int delay;

    private int queueCapacity;

    private ExecutorProperties workshop;

    public int getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(int initialDelay) {
        this.initialDelay = initialDelay;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public ExecutorProperties getWorkshop() {
        return workshop;
    }

    public void setWorkshop(ExecutorProperties workshop) {
        this.workshop = workshop;
    }

    public static class ExecutorProperties {

        private Integer corePoolSize;
        private Integer maxPoolSize;

        public Integer getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(Integer corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public Integer getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(Integer maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

    }

}
