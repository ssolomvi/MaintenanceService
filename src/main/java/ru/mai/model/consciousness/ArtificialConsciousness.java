package ru.mai.model.consciousness;

import ru.mai.model.marker.ArtificialSoulComponentMarker;

//@Table(TABLE_ARTIFICIAL_CONSCIOUSNESS)
public class ArtificialConsciousness extends Consciousness implements ArtificialSoulComponentMarker {

    public final static String TABLE_ARTIFICIAL_CONSCIOUSNESS = "artificial_consciousness";

    @Override
    public void activate() {
        System.out.println("Загрузка биологических воспоминаний...");
    }

    @Override
    public ArtificialConsciousness cloneSelf() {
        return new ArtificialConsciousness();
    }
}
