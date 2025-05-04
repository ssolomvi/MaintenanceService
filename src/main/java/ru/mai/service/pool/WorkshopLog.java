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
    private final Map<UUID, WorkshopLogEntity> workshopLog = new ConcurrentHashMap();

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

    public List<WorkshopLogEntity> getByPageAndSize(int page, int size) {
        if (page < 0 || size < 1) return Collections.emptyList();

        var fromIdx = page * size;
        var toIdx = fromIdx + size - 1;
        if (fromIdx > workshopLog.size() || toIdx >= workshopLog.size()) return Collections.emptyList();

        return new ArrayList<>(workshopLog.values()).subList(fromIdx, toIdx);
    }

    public List<WorkshopLogEntity> getRepairedByPageAndSize(int page, int size) {
        return getByStatusAndPageAndSize(RepairingStatus.SUCCESS, page, size);
    }

    public List<WorkshopLogEntity> getUtilizedByPageAndSize(int page, int size) {
        return getByStatusAndPageAndSize(RepairingStatus.FAILURE, page, size);
    }

    private List<WorkshopLogEntity> getByStatusAndPageAndSize(RepairingStatus status, int page, int size) {
        if (page < 0 || size < 1) return Collections.emptyList();

        var fromIdx = page * size;
        var toIdx = fromIdx + size - 1;

        if (fromIdx > workshopLog.size() || toIdx >= workshopLog.size()) return Collections.emptyList();

        var res = workshopLog.values().stream()
                .filter(e -> status.equals(e.getRepairingStatus()))
                .toList();

        try {
            return res.subList(fromIdx, toIdx);
        } catch (IndexOutOfBoundsException e) {
            return Collections.emptyList();
        }
    }

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
