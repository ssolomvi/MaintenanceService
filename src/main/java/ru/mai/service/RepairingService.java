package ru.mai.service;

import jakarta.annotation.PreDestroy;
import org.apache.commons.lang3.BooleanUtils;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.mai.config.property.ExecutorConfiguration;
import ru.mai.model.RepairablePrototype;
import ru.mai.model.consciousness.Consciousness;
import ru.mai.model.emotionChip.EmotionChip;
import ru.mai.model.memoryModule.MemoryModule;
import ru.mai.model.soul.Soul;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Service
@SuppressWarnings("rawtypes")
public class RepairingService {

    private static final Logger log = LoggerFactory.getLogger(RepairingService.class);

    private final WorkshopService workshopService;

    private final ComponentProvider componentProvider;

    private final BlockingQueue<Soul> queue;
    private final ScheduledExecutorService scheduledExecutor;
    private final ExecutorService executor;

    @Autowired
    public RepairingService(WorkshopService workshopService, ComponentProvider componentProvider,
                            ExecutorConfiguration executorConfiguration,
                            @Qualifier("repairingScheduledExecutor") ScheduledExecutorService scheduledExecutor,
                            @Qualifier("repairingExecutor") ExecutorService executor) {
        this.workshopService = workshopService;

        this.componentProvider = componentProvider;

        this.executor = executor;
        this.scheduledExecutor = scheduledExecutor;
        this.queue = new LinkedBlockingQueue<>(executorConfiguration.getQueueCapacity());

        scheduleQueuePolling(executorConfiguration);
    }

    public void putForRepairing(Soul soul) {
        log.trace("Got {} to fix", soul);
        try {
            queue.add(soul);
            log.info("Adding to queue for repairing: {}. Remaining capacity: {}", soul, queue.remainingCapacity());
        } catch (IllegalStateException e) {
            log.warn("Maximum capacity is reached, cannot process query for {}", soul);
        }
    }

    public List<Soul> getActiveQueriesByPageAndCount(int page, int count) {
        if (page < 0 || count < 1) {
            log.warn("Incorrect page and count parameters passed. Page >= 0, count > 0");

            return Collections.emptyList();
        }

        var snapshot = Arrays.asList(queue.toArray(new Soul[0]));

        var fromIndex = page * count;
        var toIndex = Math.min(fromIndex + count - 1, snapshot.size() - 1);

        if (fromIndex > snapshot.size() || toIndex < 0) {
            log.info("Repairing service's queue is empty");

            return Collections.emptyList();
        }

        var result = snapshot.subList(fromIndex, toIndex);
        log.info("Found {} components in repairing service's queue", result.size());

        return result;
    }

    private void scheduleQueuePolling(ExecutorConfiguration configuration) {
        scheduledExecutor.scheduleWithFixedDelay(
                this::pollAndProcessIfAny,
                configuration.getInitialDelay(),
                configuration.getDelay(),
                TimeUnit.SECONDS
        );
    }

    private void pollAndProcessIfAny() {
        Soul toRepair;
        if ((toRepair = queue.poll()) != null) {
            log.trace("Processing repairing of: {}", toRepair);
            repairSoul(toRepair);
        }
    }

    private void repairSoul(Soul soul) {
        Preconditions.checkNotNull(soul, "Soul is not passed for repairing");

        List<Future<RepairablePrototype>> results = new ArrayList<>();
        log.trace("Checking if consciousness needs curing...");
        results.add(
                executor.submit(() -> sendForRepairAndAcquireAnotherIfNeeded(soul.getConsciousness()))
        );
        log.trace("Checking if emotion chip needs curing...");
        results.add(
                executor.submit(() -> sendForRepairAndAcquireAnotherIfNeeded(soul.getEmotionChip()))
        );
        log.trace("Checking if memory module needs curing...");
        results.add(
                executor.submit(() -> sendForRepairAndAcquireAnotherIfNeeded(soul.getMemoryModule()))
        );

        try {
            for (Future<RepairablePrototype> result : results) {
                var component = result.get();

                if (component instanceof Consciousness) {
                    soul.setConsciousness((Consciousness) component);
                } else if (component instanceof EmotionChip) {
                    soul.setEmotionChip((EmotionChip) component);
                } else if (component instanceof MemoryModule) {
                    soul.setMemoryModule((MemoryModule) component);
                }

            }
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error during soul repairing: ", e);
            throw new RuntimeException(e);
        }

        log.info("Cured another soul! {}", soul);
    }

    private <T extends RepairablePrototype> T sendForRepairAndAcquireAnotherIfNeeded(T component) {
        if (isBroken(component)) {
            log.debug("Component {} is broken", component);
            if (component != null) {
                log.trace("Sending component {} to repair", component);
                workshopService.putForRepair(component);
            }

            log.trace("Acquiring another component alike {}", component);
            return componentProvider.getComponentBy(component);
        }

        log.debug("Component {} is not broken and does not need fixing", component);
        return component;
    }

    private boolean isBroken(RepairablePrototype component) {
        return component == null
                || BooleanUtils.toBooleanDefaultIfNull(component.getBroken(), true);
    }

    @PreDestroy
    private void shutdownExecutorServiceAndAwaitTermination() {
        log.info("Shutting down the scheduled executor service");

        scheduledExecutor.shutdown();
        try {
            if (!scheduledExecutor.awaitTermination(1, TimeUnit.HOURS)) {
                scheduledExecutor.shutdownNow();
            }
        } catch (InterruptedException ie) {
            scheduledExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
