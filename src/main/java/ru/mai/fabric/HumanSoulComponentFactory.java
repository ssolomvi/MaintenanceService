package ru.mai.fabric;

import org.springframework.stereotype.Component;
import ru.mai.config.property.TimeToCreateConfigurationProperties;
import ru.mai.model.consciousness.HumanConsciousness;
import ru.mai.model.emotionChip.HumanEmotionChip;
import ru.mai.model.memoryModule.HumanMemoryModule;

@Component
public class HumanSoulComponentFactory extends AbstractSoulComponentFactory {

    private static HumanSoulComponentFactory instance;

    private HumanSoulComponentFactory(TimeToCreateConfigurationProperties configuration) {
        super(configuration, new HumanConsciousness(), new HumanMemoryModule(), new HumanEmotionChip());
    }

    public static HumanSoulComponentFactory getHumanSoulComponentFactory(
            TimeToCreateConfigurationProperties configuration) {
        if (instance == null) {
            instance = new HumanSoulComponentFactory(configuration);
        }

        return instance;
    }

}
