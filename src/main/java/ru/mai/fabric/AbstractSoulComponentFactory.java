package ru.mai.fabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mai.config.property.TimeToCreateConfigurationProperties;
import ru.mai.model.RepairablePrototype;
import ru.mai.model.consciousness.Consciousness;
import ru.mai.model.emotionChip.EmotionChip;
import ru.mai.model.memoryModule.MemoryModule;

public abstract class AbstractSoulComponentFactory implements SoulComponentFactory {

    private static final Logger log = LoggerFactory.getLogger(AbstractSoulComponentFactory.class);
    private final Consciousness consciousnessPrototype;
    private final MemoryModule memoryModulePrototype;
    private final EmotionChip emotionChipPrototype;

    private final TimeToCreateConfigurationProperties configuration;

    protected AbstractSoulComponentFactory(TimeToCreateConfigurationProperties configuration,
                                           Consciousness consciousnessPrototype,
                                           MemoryModule memoryModulePrototype,
                                           EmotionChip emotionChipPrototype) {
        this.consciousnessPrototype = consciousnessPrototype;
        this.memoryModulePrototype = memoryModulePrototype;
        this.emotionChipPrototype = emotionChipPrototype;

        this.configuration = configuration;
    }

    @Override
    public Consciousness createConsciousness() {
        return imitateComponentCreation(consciousnessPrototype);
    }

    @Override
    public MemoryModule createMemoryModule() {
        return imitateComponentCreation(memoryModulePrototype);
    }

    @Override
    public EmotionChip createEmotionChip() {
        return imitateComponentCreation(emotionChipPrototype);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <T extends RepairablePrototype> T createComponentBy(T component) {
        if (component instanceof Consciousness) {
            return (T) createConsciousness();
        }
        if (component instanceof MemoryModule) {
            return (T) createMemoryModule();
        }

        return (T) createEmotionChip();
    }

    @SuppressWarnings("rawtypes")
    private <T extends RepairablePrototype> T imitateComponentCreation(T component) {
        var creationTime = component.calculateCreationTime(configuration);
        log.trace("Creating {} This will take {} ms", component, creationTime);

        try {
            Thread.sleep(creationTime);
        } catch (InterruptedException e) {
            log.warn("Component creation was interrupted, {}", e);
        }

        //noinspection unchecked
        return (T) component.cloneSelf();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
