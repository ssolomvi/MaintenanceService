package ru.mai.model.emotionChip;

import org.springframework.data.relational.core.mapping.Table;
import ru.mai.model.marker.ArtificialSoulComponentMarker;

import static ru.mai.model.emotionChip.ArtificialEmotionChip.TABLE_ARTIFICIAL_EMOTION_CHIP;

@Table(TABLE_ARTIFICIAL_EMOTION_CHIP)
public class ArtificialEmotionChip extends EmotionChip implements ArtificialSoulComponentMarker {

    public static final String TABLE_ARTIFICIAL_EMOTION_CHIP = "artificial_emotion_chip";

    @Override
    public void generateEmotion() {
        System.out.println("Симуляция эмоционального отклика...");
    }

    @Override
    public ArtificialEmotionChip cloneSelf() {
        return new ArtificialEmotionChip();
    }
}
