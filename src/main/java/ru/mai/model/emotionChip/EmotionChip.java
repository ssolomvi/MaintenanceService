package ru.mai.model.emotionChip;

import ru.mai.config.SuccessRateConfigurationProperties;
import ru.mai.config.TimeToFixConfigurationProperties;
import ru.mai.model.RepairablePrototype;

import static ru.mai.config.Utils.EMOTION_CHIP;

public abstract class EmotionChip<T> extends RepairablePrototype<T> {

    public abstract void generateEmotion();

    @Override
    public int calculateRepairSuccess(SuccessRateConfigurationProperties configuration) {
        return configuration.getByComponent().get(EMOTION_CHIP) * super.calculateRepairSuccess(configuration) / 100;
    }

    @Override
    public long calculateRepairTime(TimeToFixConfigurationProperties configuration) {
        return configuration.getByComponent().get(EMOTION_CHIP) + super.calculateRepairTime(configuration);
    }

}
