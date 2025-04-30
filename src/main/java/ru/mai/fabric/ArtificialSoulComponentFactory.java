package ru.mai.fabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mai.model.consciousness.ArtificialConsciousness;
import ru.mai.model.consciousness.Consciousness;
import ru.mai.model.emotionChip.ArtificialEmotionChip;
import ru.mai.model.emotionChip.EmotionChip;
import ru.mai.model.memoryModule.ArtificialMemoryModule;
import ru.mai.model.memoryModule.MemoryModule;

@SuppressWarnings("rawtypes")
public class ArtificialSoulComponentFactory implements SoulComponentFactory {

    private static final Logger log = LoggerFactory.getLogger(ArtificialSoulComponentFactory.class);
    private static ArtificialSoulComponentFactory instance;
    private final ArtificialConsciousness consciousnessPrototype;
    private final ArtificialMemoryModule memoryModulePrototype;
    private final ArtificialEmotionChip emotionChipPrototype;

    private ArtificialSoulComponentFactory() {
        this.consciousnessPrototype = new ArtificialConsciousness();
        this.memoryModulePrototype = new ArtificialMemoryModule();
        this.emotionChipPrototype = new ArtificialEmotionChip();
    }

    public static ArtificialSoulComponentFactory getArtificialSoulComponentFactory() {
        if (instance == null) {
            instance = new ArtificialSoulComponentFactory();
        }

        return instance;
    }

    @Override
    public Consciousness createConsciousness() {
        log.trace("Creating artificial consciousness by prototype... so alike");
        return consciousnessPrototype.cloneSelf();
    }

    @Override
    public MemoryModule createMemoryModule() {
        log.trace("Creating artificial memory module by prototype... so vast");
        return memoryModulePrototype.cloneSelf();
    }

    @Override
    public EmotionChip createEmotionChip() {
        log.trace("Creating artificial emotion chip by prototype... so emotionless");
        return emotionChipPrototype.cloneSelf();
    }
}
