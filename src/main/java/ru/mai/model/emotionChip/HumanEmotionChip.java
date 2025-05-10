package ru.mai.model.emotionChip;

import ru.mai.model.marker.HumanSoulComponentMarker;

//@Table(TABLE_HUMAN_EMOTION_CHIP)
public class HumanEmotionChip extends EmotionChip implements HumanSoulComponentMarker {

    public static final String TABLE_HUMAN_EMOTION_CHIP = "human_emotion_chip";

    @Override
    public void generateEmotion() {
        System.out.println("Генерация человеческих эмоций...");
    }

    @Override
    public HumanEmotionChip cloneSelf() {
        return new HumanEmotionChip();
    }

}
