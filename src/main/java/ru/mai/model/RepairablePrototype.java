package ru.mai.model;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.mai.config.property.SuccessRateConfigurationProperties;
import ru.mai.config.property.TimeToCreateConfigurationProperties;
import ru.mai.config.property.TimeToFixConfigurationProperties;
import ru.mai.model.marker.ArtificialSoulComponentMarker;
import ru.mai.model.marker.Prototype;

import java.util.UUID;

import static ru.mai.config.Utils.ARTIFICIAL;
import static ru.mai.config.Utils.HUMAN;

public abstract class RepairablePrototype<T> implements Prototype<T> {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id = UUID.randomUUID();
    private Boolean broken = false;

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

    public long calculateCreationTime(TimeToCreateConfigurationProperties configuration) {
        return this instanceof ArtificialSoulComponentMarker
                ? configuration.getByType().get(ARTIFICIAL)
                : configuration.getByType().get(HUMAN);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getBroken() {
        return broken;
    }

    public void setBroken(Boolean broken) {
        this.broken = broken;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", getClass().getSimpleName(), id);
    }

}
