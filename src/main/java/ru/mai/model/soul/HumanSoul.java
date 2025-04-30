package ru.mai.model.soul;

import ru.mai.model.consciousness.HumanConsciousness;
import ru.mai.model.emotionChip.HumanEmotionChip;
import ru.mai.model.memoryModule.HumanMemoryModule;

public class HumanSoul extends Soul {

    public HumanSoul(HumanConsciousness consciousness,
                     HumanEmotionChip emotionChip,
                     HumanMemoryModule memoryModule) {
        super(consciousness, emotionChip, memoryModule);
    }

}
