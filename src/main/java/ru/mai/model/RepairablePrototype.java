package ru.mai.model;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.mai.config.SuccessRateConfigurationProperties;
import ru.mai.config.TimeToFixConfigurationProperties;
import ru.mai.model.marker.ArtificialSoulComponentMarker;
import ru.mai.model.marker.Prototype;

import java.time.LocalDateTime;

import static ru.mai.config.Utils.ARTIFICIAL;
import static ru.mai.config.Utils.HUMAN;

public abstract class RepairablePrototype<T> implements Prototype<T> {

//    private Long id;
    private Boolean broken = false;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private RepairingResult repairingResult;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime repairingStartTime;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime repairingEndTime;

    public int calculateRepairSuccess(SuccessRateConfigurationProperties configuration) {
        return this instanceof ArtificialSoulComponentMarker
                ? configuration.getByType().get(ARTIFICIAL)
                : configuration.getByType().get(HUMAN);
    }

    public long calculateRepairTime(TimeToFixConfigurationProperties configuration) {
        return this instanceof ArtificialSoulComponentMarker
                ? configuration.getByType().get(ARTIFICIAL)
                : configuration.getByType().get(HUMAN);
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public Boolean getBroken() {
        return broken;
    }

    public void setBroken(Boolean broken) {
        this.broken = broken;
    }

    public RepairingResult getRepairingResult() {
        return repairingResult;
    }

    public void setRepairingResult(RepairingResult repairingResult) {
        this.repairingResult = repairingResult;
    }

    public LocalDateTime getRepairingStartTime() {
        return repairingStartTime;
    }

    public void setRepairingStartTime(LocalDateTime repairingStartTime) {
        this.repairingStartTime = repairingStartTime;
    }

    public LocalDateTime getRepairingEndTime() {
        return repairingEndTime;
    }

    public void setRepairingEndTime(LocalDateTime repairingEndTime) {
        this.repairingEndTime = repairingEndTime;
    }

}
