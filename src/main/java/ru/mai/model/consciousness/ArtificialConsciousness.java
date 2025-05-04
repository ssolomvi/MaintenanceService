package ru.mai.model.consciousness;

import org.springframework.data.relational.core.mapping.Table;
import ru.mai.model.marker.ArtificialSoulComponentMarker;

import static ru.mai.model.consciousness.ArtificialConsciousness.TABLE_ARTIFICIAL_CONSCIOUSNESS;

@Table(TABLE_ARTIFICIAL_CONSCIOUSNESS)
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
