package ru.mai.model.emotionChip;

import ru.mai.config.property.SuccessRateConfigurationProperties;
import ru.mai.config.property.TimeToCreateConfigurationProperties;
import ru.mai.config.property.TimeToFixConfigurationProperties;
import ru.mai.model.RepairablePrototype;

import static ru.mai.config.Utils.EMOTION_CHIP;

public abstract class EmotionChip extends RepairablePrototype<EmotionChip> {

    public abstract void generateEmotion();

    @Override
    public int calculateRepairSuccess(SuccessRateConfigurationProperties configuration) {
        return configuration.getByComponent().get(EMOTION_CHIP) * super.calculateRepairSuccess(configuration) / 100;
    }

    @Override
    public long calculateRepairTime(TimeToFixConfigurationProperties configuration) {
        return configuration.getByComponent().get(EMOTION_CHIP) * super.calculateRepairTime(configuration);
    }

    @Override
    public long calculateCreationTime(TimeToCreateConfigurationProperties configuration) {
        return configuration.getByComponent().get(EMOTION_CHIP) * super.calculateCreationTime(configuration);
    }

}
