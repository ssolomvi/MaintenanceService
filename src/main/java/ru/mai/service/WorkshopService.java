package ru.mai.service;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.mai.config.property.ExecutorConfiguration;
import ru.mai.config.property.SuccessRateConfigurationProperties;
import ru.mai.config.property.TimeToFixConfigurationProperties;
import ru.mai.model.RepairablePrototype;
import ru.mai.model.enums.RepairingStatus;
import ru.mai.service.pool.ComponentPool;
import ru.mai.service.pool.WorkshopLog;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@SuppressWarnings("rawtypes")
public class WorkshopService {

    private static final Logger log = LoggerFactory.getLogger(WorkshopService.class);
    private static final NumberFormat nFormat = NumberFormat.getInstance();

    private final ComponentPool componentPool;
    private final WorkshopLog workshopLog;

    private final SuccessRateConfigurationProperties successRateConfigurationProperties;
    private final TimeToFixConfigurationProperties timeToFixConfigurationProperties;

    private final BlockingQueue<RepairablePrototype> queue;
    private final ScheduledExecutorService executor;

    @Autowired
    public WorkshopService(ComponentPool componentPool,
                           WorkshopLog workshopLog,
                           SuccessRateConfigurationProperties successRateConfigurationProperties,
                           TimeToFixConfigurationProperties timeToFixConfigurationProperties,
                           ExecutorConfiguration executorConfiguration,
                           @Qualifier("workshopScheduledExecutor") ScheduledExecutorService executor) {
        this.componentPool = componentPool;
        this.workshopLog = workshopLog;

        this.successRateConfigurationProperties = successRateConfigurationProperties;
        this.timeToFixConfigurationProperties = timeToFixConfigurationProperties;

        this.executor = executor;
        this.queue = new LinkedBlockingQueue<>(executorConfiguration.getQueueCapacity());

        scheduleQueuePolling(executorConfiguration);
    }

    public <T extends RepairablePrototype> void putForRepair(T component) {
        log.trace("Got {} to fix", component);
        try {
            queue.add(component);
            workshopLog.put(component);
            log.info("Adding to queue for repairing: {}. Remaining capacity: {}", component, queue.remainingCapacity());
        } catch (IllegalStateException e) {
            log.warn("Maximum capacity is reached, cannot process query for {}", component);
        }
    }

    public List<RepairablePrototype> getActiveQueriesByPageAndCount(int page, int count) {
        if (page < 0 || count < 1) {
            log.warn("Incorrect page and count parameters passed. Page >= 0, count > 0");

            return Collections.emptyList();
        }
        var snapshot = Arrays.asList(queue.toArray(new RepairablePrototype[0]));

        var fromIndex = page * count;
        var toIndex = Math.min(fromIndex + count - 1, snapshot.size() - 1);

        if (fromIndex > snapshot.size() || toIndex < 0) {
            log.info("Workshop's queue is empty");

            return Collections.emptyList();
        }

        var result = snapshot.subList(fromIndex, toIndex);
        log.info("Found {} components in workshop's queue", result.size());

        return result;
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
            handleComponentRepairing(toRepair);
        }
    }

    private <T extends RepairablePrototype> void handleComponentRepairing(T component) {
        var repairIsSuccessful = repair(component);

        log.info("Repair result is {} for {}", repairIsSuccessful ? "successful" : "unsuccessful", component);

        if (repairIsSuccessful) {
            handleRepairIsSuccessful(component);
        } else {
            handleRepairIsNotSuccessful(component);
        }
    }

    private <T extends RepairablePrototype> void handleRepairIsSuccessful(T component) {
        component.setBroken(false);
        componentPool.put(component);
        workshopLog.refreshComponentStatus(component.getId(), RepairingStatus.SUCCESS);
        log.trace("Put cured component {} to pool!", component);
    }

    private <T extends RepairablePrototype> void handleRepairIsNotSuccessful(T component) {
        component.setBroken(true);
        workshopLog.refreshComponentStatus(component.getId(), RepairingStatus.FAILURE);
        log.trace("Soul component {} is not usable anymore... what a pity... sending for utilization!", component);
    }

    private boolean repair(RepairablePrototype component) {
        var estimatedRepairingTime = calculateRepairingTime(component);
        var successRate = calculateRepairSuccessRate(component);
        log.info("Estimated repairing time is: {} ms with success rate: {}% for {}",
                nFormat.format(estimatedRepairingTime), successRate, component);

        try {
            Thread.sleep(estimatedRepairingTime);
        } catch (InterruptedException e) {
            log.error("Critical error, comparable to burnout, has happened while trying to repair: {}", e.getMessage());

            throw new RuntimeException(e);
        }

        return repairIsSuccess(successRate);
    }

    private boolean repairIsSuccess(int rate) {
        return new Random().nextInt(100) < rate;
    }

    private int calculateRepairSuccessRate(RepairablePrototype component) {
        return component.calculateRepairSuccess(successRateConfigurationProperties);
    }

    private long calculateRepairingTime(RepairablePrototype component) {
        return component.calculateRepairTime(timeToFixConfigurationProperties);
    }

    @PreDestroy
    private void shutdownExecutorServiceAndAwaitTermination() {
        log.info("Shutting down the scheduled executor service");

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
