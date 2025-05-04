package ru.mai.model.memoryModule;

import org.springframework.data.relational.core.mapping.Table;
import ru.mai.model.marker.ArtificialSoulComponentMarker;

import static ru.mai.model.memoryModule.ArtificialMemoryModule.TABLE_ARTIFICIAL_MEMORY_MODULE;

@Table(TABLE_ARTIFICIAL_MEMORY_MODULE)
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
