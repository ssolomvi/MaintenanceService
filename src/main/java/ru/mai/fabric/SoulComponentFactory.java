package ru.mai.fabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mai.model.RepairablePrototype;
import ru.mai.model.consciousness.Consciousness;
import ru.mai.model.emotionChip.EmotionChip;
import ru.mai.model.memoryModule.MemoryModule;

@SuppressWarnings("rawtypes")
public interface SoulComponentFactory {


    Consciousness createConsciousness();

    MemoryModule createMemoryModule();

    EmotionChip createEmotionChip();

    @SuppressWarnings("unchecked")
    default <T extends RepairablePrototype> T createComponentBy(T component) {
        if (component instanceof Consciousness<?>) {
            return (T) createConsciousness();
        }
        if (component instanceof MemoryModule<?>) {
            return (T) createMemoryModule();
        }

        return (T) createEmotionChip();
    }

}
