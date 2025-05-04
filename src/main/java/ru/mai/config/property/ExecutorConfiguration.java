package ru.mai.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.executor")
public class ExecutorConfiguration {

    private int initialDelay;

    private int delay;

    private int queueCapacity;

    private ScheduledExecutorProperties workshopScheduled;
    private ScheduledExecutorProperties repairingServiceScheduled;
    private ExecutorProperties repairingService;

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

    public ScheduledExecutorProperties getWorkshopScheduled() {
        return workshopScheduled;
    }

    public void setWorkshopScheduled(ScheduledExecutorProperties workshopScheduled) {
        this.workshopScheduled = workshopScheduled;
    }

    public ScheduledExecutorProperties getRepairingServiceScheduled() {
        return repairingServiceScheduled;
    }

    public void setRepairingServiceScheduled(ScheduledExecutorProperties repairingServiceScheduled) {
        this.repairingServiceScheduled = repairingServiceScheduled;
    }

    public ExecutorProperties getRepairingService() {
        return repairingService;
    }

    public void setRepairingService(ExecutorProperties repairingService) {
        this.repairingService = repairingService;
    }

    public static class ScheduledExecutorProperties {

        private Integer corePoolSize;
        private Integer maxPoolSize;
        private Integer queueCapacity;

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

        public Integer getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(Integer queueCapacity) {
            this.queueCapacity = queueCapacity;
        }

    }

    public static class ExecutorProperties extends ScheduledExecutorProperties {

        private Integer keepAliveTime;

        public Integer getKeepAliveTime() {
            return keepAliveTime;
        }

        public void setKeepAliveTime(Integer keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }

    }

}
