package ru.mai.service;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.mai.config.ExecutorConfiguration;
import ru.mai.config.SuccessRateConfigurationProperties;
import ru.mai.config.TimeToFixConfigurationProperties;
import ru.mai.model.RepairablePrototype;
import ru.mai.model.RepairingResult;
import ru.mai.service.pool.ComponentPool;
import ru.mai.service.pool.UtilizationPool;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@SuppressWarnings("rawtypes")
public class WorkshopService {

    private static final Logger log = LoggerFactory.getLogger(WorkshopService.class);
    private final ComponentPool componentPool;
    private final UtilizationPool utilizationPool;

    private final SuccessRateConfigurationProperties successRateConfigurationProperties;
    private final TimeToFixConfigurationProperties timeToFixConfigurationProperties;

    private final BlockingQueue<RepairablePrototype> queue;
    private final ScheduledExecutorService executor;

    @Autowired
    public WorkshopService(ComponentPool componentPool,
                           UtilizationPool utilizationPool,
                           SuccessRateConfigurationProperties successRateConfigurationProperties,
                           TimeToFixConfigurationProperties timeToFixConfigurationProperties,
                           ExecutorConfiguration executorConfiguration,
                           @Qualifier("workshopExecutor") ScheduledExecutorService executor) {
        this.componentPool = componentPool;
        this.utilizationPool = utilizationPool;

        this.successRateConfigurationProperties = successRateConfigurationProperties;
        this.timeToFixConfigurationProperties = timeToFixConfigurationProperties;

        this.executor = executor;
        this.queue = new LinkedBlockingQueue<>();

        scheduleQueuePolling(executorConfiguration);
    }

    public <T extends RepairablePrototype> void putForRepair(T component) {
        log.trace("Got {} to fix", component);
        queue.add(component);
    }

    private void scheduleQueuePolling(ExecutorConfiguration configuration) {
        executor.scheduleWithFixedDelay(
                this::pollAndProcessIfAny,
                configuration.getInitialDelay(),
                configuration.getDelay(),
                TimeUnit.SECONDS
        );
    }

    private void pollAndProcessIfAny() {
        RepairablePrototype toRepair;
        if ((toRepair = queue.poll()) != null) {
            log.trace("Processing repairing of: {}", toRepair);
            fix(toRepair);
        }
    }

    private <T extends RepairablePrototype> void fix(T component) {
        component.setRepairingStartTime(LocalDateTime.now());

        var repairIsSuccessful = repair(component);

        component.setRepairingEndTime(LocalDateTime.now());

        log.info("Repair result is {} for {}", repairIsSuccessful ? "successful" : "unsuccessful", component);

        if (repairIsSuccessful) {
            handleRepairIsSuccessful(component);
        } else {
            handleRepairIsNotSuccessful(component);
        }
    }

    private <T extends RepairablePrototype> void handleRepairIsSuccessful(T component) {
        component.setBroken(false);
        component.setRepairingResult(RepairingResult.REPAIRED);
        componentPool.put(component);
        log.trace("Put cured component {} to pool!", component);
    }

    private <T extends RepairablePrototype> void handleRepairIsNotSuccessful(T component) {
        component.setBroken(true);
        component.setRepairingResult(RepairingResult.FAILED);
        utilizationPool.put(component);
        log.trace("Soul component {} is not usable anymore... what a pity... sending for utilization!", component);
    }

    private long estimateRepairingTime(RepairablePrototype component) {
        return component.calculateRepairTime(timeToFixConfigurationProperties);
    }

    private boolean repair(RepairablePrototype component) {
        try {
            var estimatedRepairingTime = estimateRepairingTime(component);
            log.debug("Estimated repairing time is {} s for {}", estimatedRepairingTime, component);
            Thread.sleep(estimateRepairingTime(component) * 1000);
        } catch (InterruptedException e) {
            log.error("Critical error, comparable to burnout, has happened while trying to repair: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        return calculateRepairSuccessWithSuccessRate(component);
    }

    private boolean calculateRepairSuccessWithSuccessRate(RepairablePrototype component) {
        var rate = component.calculateRepairSuccess(successRateConfigurationProperties);
        log.trace("Success rate is {} for component {}", rate, component);
        return new Random().nextInt(100) < rate;
    }

    @PreDestroy
    private void shutdownExecutorServiceAndAwaitTermination() {
        log.debug("Shutting down the scheduled executor service");

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
