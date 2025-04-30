package ru.mai.model.consciousness;

import ru.mai.config.SuccessRateConfigurationProperties;
import ru.mai.config.TimeToFixConfigurationProperties;
import ru.mai.model.RepairablePrototype;

import static ru.mai.config.Utils.CONSCIOUSNESS;

public abstract class Consciousness<T> extends RepairablePrototype<T> {

    public abstract void activate();

    @Override
    public int calculateRepairSuccess(SuccessRateConfigurationProperties configuration) {
        return configuration.getByComponent().get(CONSCIOUSNESS) * super.calculateRepairSuccess(configuration) / 100;
    }

    @Override
    public long calculateRepairTime(TimeToFixConfigurationProperties configuration) {
        return configuration.getByComponent().get(CONSCIOUSNESS) + super.calculateRepairTime(configuration);
    }

}
