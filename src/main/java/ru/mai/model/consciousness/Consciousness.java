package ru.mai.model.consciousness;

import ru.mai.config.property.SuccessRateConfigurationProperties;
import ru.mai.config.property.TimeToCreateConfigurationProperties;
import ru.mai.config.property.TimeToFixConfigurationProperties;
import ru.mai.model.RepairablePrototype;

import static ru.mai.config.Utils.CONSCIOUSNESS;

public abstract class Consciousness extends RepairablePrototype<Consciousness> {

    public abstract void activate();

    @Override
    public int calculateRepairSuccess(SuccessRateConfigurationProperties configuration) {
        return configuration.getByComponent().get(CONSCIOUSNESS) * super.calculateRepairSuccess(configuration) / 100;
    }

    @Override
    public long calculateRepairTime(TimeToFixConfigurationProperties configuration) {
        return configuration.getByComponent().get(CONSCIOUSNESS) + super.calculateRepairTime(configuration);
    }

    @Override
    public long calculateCreationTime(TimeToCreateConfigurationProperties configuration) {
        return configuration.getByComponent().get(CONSCIOUSNESS) * super.calculateCreationTime(configuration);
    }

}
