package ru.mai.fabric;

import ru.mai.model.RepairablePrototype;
import ru.mai.model.consciousness.Consciousness;
import ru.mai.model.emotionChip.EmotionChip;
import ru.mai.model.memoryModule.MemoryModule;

@SuppressWarnings("rawtypes")
public interface SoulComponentFactory {

    // todo: configure creation time for components

    Consciousness createConsciousness();

    MemoryModule createMemoryModule();

    EmotionChip createEmotionChip();

    <T extends RepairablePrototype> T createComponentBy(T component);

}
