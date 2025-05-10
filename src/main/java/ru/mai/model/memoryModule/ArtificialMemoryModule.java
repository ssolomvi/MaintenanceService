package ru.mai.model.memoryModule;

import ru.mai.model.marker.ArtificialSoulComponentMarker;

//@Table(TABLE_ARTIFICIAL_MEMORY_MODULE)
public class ArtificialMemoryModule extends MemoryModule implements ArtificialSoulComponentMarker {

    public static final String TABLE_ARTIFICIAL_MEMORY_MODULE = "artificial_memory_module";

    @Override
    public void loadMemories() {
        System.out.println("Загрузка ИИ-сознания...");
    }

    @Override
    public ArtificialMemoryModule cloneSelf() {
        return new ArtificialMemoryModule();
    }

}
