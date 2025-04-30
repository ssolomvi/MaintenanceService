package ru.mai.model.memoryModule;

import ru.mai.config.SuccessRateConfigurationProperties;
import ru.mai.config.TimeToFixConfigurationProperties;
import ru.mai.model.RepairablePrototype;

import static ru.mai.config.Utils.MEMORY_MODULE;

public abstract class MemoryModule<T> extends RepairablePrototype<T> {

    public abstract void loadMemories();

    @Override
    public int calculateRepairSuccess(SuccessRateConfigurationProperties configuration) {
        return configuration.getByComponent().get(MEMORY_MODULE) * super.calculateRepairSuccess(configuration) / 100;
    }

    @Override
    public long calculateRepairTime(TimeToFixConfigurationProperties configuration) {
        return configuration.getByComponent().get(MEMORY_MODULE) + super.calculateRepairTime(configuration);
    }

}
