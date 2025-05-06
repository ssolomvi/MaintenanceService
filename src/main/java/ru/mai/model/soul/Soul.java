package ru.mai.model.soul;

import ru.mai.model.consciousness.Consciousness;
import ru.mai.model.emotionChip.EmotionChip;
import ru.mai.model.memoryModule.MemoryModule;

public class Soul {

    private Consciousness consciousness;
    private EmotionChip emotionChip;
    private MemoryModule memoryModule;

    public Soul(Consciousness consciousness, EmotionChip emotionChip, MemoryModule memoryModule) {
        this.consciousness = consciousness;
        this.emotionChip = emotionChip;
        this.memoryModule = memoryModule;
    }

    public Consciousness getConsciousness() {
        return consciousness;
    }

    public void setConsciousness(Consciousness consciousness) {
        this.consciousness = consciousness;
    }

    public EmotionChip getEmotionChip() {
        return emotionChip;
    }

    public void setEmotionChip(EmotionChip emotionChip) {
        this.emotionChip = emotionChip;
    }

    public MemoryModule getMemoryModule() {
        return memoryModule;
    }

    public void setMemoryModule(MemoryModule memoryModule) {
        this.memoryModule = memoryModule;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
