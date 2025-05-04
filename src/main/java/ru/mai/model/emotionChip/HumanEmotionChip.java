package ru.mai.model.emotionChip;

import org.springframework.data.relational.core.mapping.Table;
import ru.mai.model.marker.HumanSoulComponentMarker;

import static ru.mai.model.emotionChip.HumanEmotionChip.TABLE_HUMAN_EMOTION_CHIP;

@Table(TABLE_HUMAN_EMOTION_CHIP)
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
