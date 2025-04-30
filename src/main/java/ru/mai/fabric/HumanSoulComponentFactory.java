package ru.mai.fabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.mai.model.consciousness.Consciousness;
import ru.mai.model.consciousness.HumanConsciousness;
import ru.mai.model.emotionChip.EmotionChip;
import ru.mai.model.emotionChip.HumanEmotionChip;
import ru.mai.model.memoryModule.HumanMemoryModule;
import ru.mai.model.memoryModule.MemoryModule;

@SuppressWarnings("rawtypes")
@Component
public class HumanSoulComponentFactory implements SoulComponentFactory {

    private static final Logger log = LoggerFactory.getLogger(HumanSoulComponentFactory.class);
    private static HumanSoulComponentFactory instance;
    private final HumanConsciousness consciousnessPrototype;
    private final HumanMemoryModule memoryModulePrototype;
    private final HumanEmotionChip emotionChipPrototype;

    private HumanSoulComponentFactory() {
        this.consciousnessPrototype = new HumanConsciousness();
        this.memoryModulePrototype = new HumanMemoryModule();
        this.emotionChipPrototype = new HumanEmotionChip();
    }

    public static HumanSoulComponentFactory getHumanSoulComponentFactory() {
        if (instance == null) {
            instance = new HumanSoulComponentFactory();
        }

        return instance;
    }

    @Override
    public Consciousness createConsciousness() {
        log.trace("Creating human consciousness by prototype... now like new one...");
        return consciousnessPrototype.cloneSelf();
    }

    @Override
    public MemoryModule createMemoryModule() {
        log.trace("Creating human memory module by prototype... so small and fragmented");
        return memoryModulePrototype.cloneSelf();
    }

    @Override
    public EmotionChip createEmotionChip() {
        log.trace("Creating human emotion chip by prototype... so vivid");
        return emotionChipPrototype.cloneSelf();
    }

}
