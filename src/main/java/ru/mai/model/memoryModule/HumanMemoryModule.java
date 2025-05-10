package ru.mai.model.memoryModule;

import ru.mai.model.marker.HumanSoulComponentMarker;

//@Table(TABLE_HUMAN_MEMORY_MODULE)
public class HumanMemoryModule extends MemoryModule implements HumanSoulComponentMarker {

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
