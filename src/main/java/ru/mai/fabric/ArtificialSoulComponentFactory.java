package ru.mai.fabric;

import org.springframework.stereotype.Component;
import ru.mai.config.property.TimeToCreateConfigurationProperties;
import ru.mai.model.consciousness.ArtificialConsciousness;
import ru.mai.model.emotionChip.ArtificialEmotionChip;
import ru.mai.model.memoryModule.ArtificialMemoryModule;

@Component
public class ArtificialSoulComponentFactory extends AbstractSoulComponentFactory {

    private static ArtificialSoulComponentFactory instance;

    private ArtificialSoulComponentFactory(TimeToCreateConfigurationProperties configuration) {
        super(
                configuration,
                new ArtificialConsciousness(),
                new ArtificialMemoryModule(),
                new ArtificialEmotionChip()
        );
    }

    public static ArtificialSoulComponentFactory getArtificialSoulComponentFactory(
            TimeToCreateConfigurationProperties configuration) {
        if (instance == null) {
            instance = new ArtificialSoulComponentFactory(configuration);
        }

        return instance;
    }

}
