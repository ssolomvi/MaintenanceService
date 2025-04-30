package ru.mai.model.consciousness;

import org.springframework.data.relational.core.mapping.Table;
import ru.mai.model.marker.HumanSoulComponentMarker;

import static ru.mai.model.consciousness.HumanConsciousness.TABLE_HUMAN_CONSCIOUSNESS;

@Table(TABLE_HUMAN_CONSCIOUSNESS)
public class HumanConsciousness extends Consciousness<HumanConsciousness> implements HumanSoulComponentMarker {

    public final static String TABLE_HUMAN_CONSCIOUSNESS = "human_consciousness";

    @Override
    public void activate() {
        System.out.println("Активируется человеческое сознание...");
    }

    @Override
    public HumanConsciousness cloneSelf() {
        return new HumanConsciousness();
    }

}
