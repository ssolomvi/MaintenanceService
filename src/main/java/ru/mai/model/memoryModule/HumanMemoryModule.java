package ru.mai.model.memoryModule;

import org.springframework.data.relational.core.mapping.Table;
import ru.mai.model.marker.HumanSoulComponentMarker;

import static ru.mai.model.memoryModule.HumanMemoryModule.TABLE_HUMAN_MEMORY_MODULE;

@Table(TABLE_HUMAN_MEMORY_MODULE)
public class HumanMemoryModule extends MemoryModule<HumanMemoryModule> implements HumanSoulComponentMarker {

    public static final String TABLE_HUMAN_MEMORY_MODULE = "human_memory_module";

    @Override
    public void loadMemories() {
        System.out.println("Загрузка цифровых воспоминаний из базы данных...");
    }

    @Override
    public HumanMemoryModule cloneSelf() {
        return new HumanMemoryModule();
    }

}
