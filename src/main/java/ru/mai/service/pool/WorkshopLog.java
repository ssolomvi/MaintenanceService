package ru.mai.service.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.mai.model.RepairablePrototype;
import ru.mai.model.enums.RepairingStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class WorkshopLog {

    private static final Logger log = LoggerFactory.getLogger(WorkshopLog.class);
    private final Map<UUID, WorkshopLogEntity> workshopLog = new ConcurrentHashMap<>();

    public void put(RepairablePrototype component) {
        var log = new WorkshopLogEntity();
        log.setComponent(component);
        log.setRepairingStartDatetime(LocalDateTime.now());
        log.setRepairingStatus(RepairingStatus.PENDING);

        workshopLog.put(component.getId(), log);
    }

    public void refreshComponentStatus(UUID id, RepairingStatus status) {
        var component = workshopLog.get(id);
        if (component == null) {
            var msg = String.format("Component by id: %s cannot be found", id);
            log.error(msg);

            throw new IllegalArgumentException(msg);
        }

        component.setRepairingEndDatetime(LocalDateTime.now());
        component.setRepairingStatus(status);

        workshopLog.put(id, component);
    }

    public List<WorkshopLogEntity> getByPageAndSize(int page, int count) {
        return getByPageAndSizeInner(page, count, null);
    }

    public List<WorkshopLogEntity> getRepairedByPageAndSize(int page, int count) {
        return getByPageAndSizeInner(page, count, RepairingStatus.SUCCESS);
    }

    public List<WorkshopLogEntity> getUtilizedByPageAndSize(int page, int count) {
        return getByPageAndSizeInner(page, count, RepairingStatus.FAILURE);
    }

    public List<WorkshopLogEntity> getByPageAndSizeInner(int page, int count, RepairingStatus status) {
        if (page < 0 || count < 1) {
            log.warn("Incorrect page and count parameters passed. Page >= 0, count > 0");

            return Collections.emptyList();
        }

        var snapshot = workshopLog.values();

        if (status != null) {
            snapshot = snapshot.stream()
                    .filter(e -> status.equals(e.getRepairingStatus()))
                    .toList();
        }

        var fromIndex = page * count;
        var toIndex = Math.min(fromIndex + count - 1, snapshot.size() - 1);

        if (fromIndex > snapshot.size() || toIndex < 0) {
            if (status != null) {
                log.info("Found no components in workshop's log with status {}", status);
            } else {
                log.info("Workshop's log is empty");
            }

            return Collections.emptyList();
        }

        var result = new ArrayList<>(snapshot).subList(fromIndex, toIndex);
        if (status != null) {
            log.info("Found {} components in workshop's log with status {}", result.size(), status);
        } else {
            log.info("Found {} components in workshop's log", result.size());
        }

        return result;
    }

    @SuppressWarnings("rawtypes")
    public static class WorkshopLogEntity {

        private RepairablePrototype component;
        private LocalDateTime repairingStartDatetime;
        private LocalDateTime repairingEndDatetime;
        private RepairingStatus repairingStatus;

        public RepairablePrototype getComponent() {
            return component;
        }

        public void setComponent(RepairablePrototype component) {
            this.component = component;
        }

        public LocalDateTime getRepairingStartDatetime() {
            return repairingStartDatetime;
        }

        public void setRepairingStartDatetime(LocalDateTime repairingStartDatetime) {
            this.repairingStartDatetime = repairingStartDatetime;
        }

        public LocalDateTime getRepairingEndDatetime() {
            return repairingEndDatetime;
        }

        public void setRepairingEndDatetime(LocalDateTime repairingEndDatetime) {
            this.repairingEndDatetime = repairingEndDatetime;
        }

        public RepairingStatus getRepairingStatus() {
            return repairingStatus;
        }

        public void setRepairingStatus(RepairingStatus repairingStatus) {
            this.repairingStatus = repairingStatus;
        }

    }

}
