package ru.mai.service.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.mai.model.RepairablePrototype;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("rawtypes")
@Component
public class UtilizationPool {

    private static final Logger log = LoggerFactory.getLogger(UtilizationPool.class);
    private final Map<Class<?>, List<RepairablePrototype>> pools = new HashMap<>();

    private final ReentrantLock lock = new ReentrantLock();

    public void put(RepairablePrototype component) {
        try {
            lock.lock();

            var pool = pools.get(component.getClass());
            if (pool == null) {
                pools.put(component.getClass(), new ArrayList<>(Collections.singleton(component)));
            } else {
                pool.add(component);
            }
            log.trace("Put component {} to the pool", component);
        } finally {
            lock.unlock();
        }

    }

}
