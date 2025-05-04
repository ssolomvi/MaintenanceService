package ru.mai.service.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.mai.model.RepairablePrototype;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
@Repository
public class ComponentPool {

    private static final Logger log = LoggerFactory.getLogger(ComponentPool.class);
    private final Map<Class<?>, List<RepairablePrototype>> pools = new HashMap<>();

    private final ReentrantLock lock = new ReentrantLock();

    @Nullable
    public RepairablePrototype getNonBrokenComponentByClass(Class<?> klass) {
        try {
            lock.lock();

            var pool = pools.get(klass);
            if (pool == null) return null;

            var iterator = pool.iterator();
            RepairablePrototype found = null, curr;
            while (iterator.hasNext()) {
                curr = iterator.next();
                if (!curr.getBroken()) {
                    found = curr;
                    iterator.remove();
                    log.trace("Found appropriate component in the pool: {}", found);
                    break;
                }
            }

            return found;
        } finally {
            lock.unlock();
        }
    }

    public void put(RepairablePrototype component) {
        try {
            lock.lock();

            var pool = pools.get(component.getClass());
            if (pool == null) {
                pools.put(component.getClass(), new ArrayList<>(Collections.singleton(component)));
            } else {
                pool.add(component);
            }
            log.trace("Put component {} to component pool", component);
        } finally {
            lock.unlock();
        }

    }

    public List<RepairablePrototype> getByPageAndCount(int page, int count) {
        try {
            lock.lock();

            if (page < 0 || count < 1) {
                return Collections.emptyList();
            }

            long skip = (long) page * count;
            return pools.values().stream()
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .skip(skip)
                    .limit(count)
                    .collect(Collectors.toList());
        } finally {
            lock.unlock();
        }
    }

}
