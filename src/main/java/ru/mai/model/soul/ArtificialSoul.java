package ru.mai.model.soul;

import ru.mai.model.consciousness.ArtificialConsciousness;
import ru.mai.model.emotionChip.ArtificialEmotionChip;
import ru.mai.model.memoryModule.ArtificialMemoryModule;

public class ArtificialSoul extends Soul {

    public ArtificialSoul(ArtificialConsciousness consciousness,
                          ArtificialEmotionChip emotionChip,
                          ArtificialMemoryModule memoryModule) {
        super(consciousness, emotionChip, memoryModule);
    }

}
