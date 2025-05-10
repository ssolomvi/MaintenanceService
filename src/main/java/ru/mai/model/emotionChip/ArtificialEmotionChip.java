package ru.mai.model.emotionChip;

import ru.mai.model.marker.ArtificialSoulComponentMarker;

//@Table(TABLE_ARTIFICIAL_EMOTION_CHIP)
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
