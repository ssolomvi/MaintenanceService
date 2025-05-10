package ru.mai.model.consciousness;

import ru.mai.model.marker.HumanSoulComponentMarker;

//@Table(TABLE_HUMAN_CONSCIOUSNESS)
public class HumanConsciousness extends Consciousness implements HumanSoulComponentMarker {

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
