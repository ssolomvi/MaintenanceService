package ru.mai.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mai.config.property.TimeToCreateConfigurationProperties;
import ru.mai.fabric.ArtificialSoulComponentFactory;
import ru.mai.fabric.HumanSoulComponentFactory;
import ru.mai.fabric.SoulComponentFactory;
import ru.mai.model.RepairablePrototype;
import ru.mai.model.marker.ArtificialSoulComponentMarker;
import ru.mai.service.pool.ComponentPool;

@Service
public class ComponentProvider {

    private static final Logger log = LoggerFactory.getLogger(ComponentProvider.class);

    private final ArtificialSoulComponentFactory artificialSoulComponentFactory;
    private final HumanSoulComponentFactory humanSoulComponentFactory;

    private final ComponentPool componentPool;

    @Autowired
    public ComponentProvider(TimeToCreateConfigurationProperties timeToCreateConfigurationProperties,
                             ComponentPool componentPool) {
        this.artificialSoulComponentFactory = ArtificialSoulComponentFactory
                .getArtificialSoulComponentFactory(timeToCreateConfigurationProperties);
        this.humanSoulComponentFactory = HumanSoulComponentFactory
                .getHumanSoulComponentFactory(timeToCreateConfigurationProperties);

        this.componentPool = componentPool;
    }

    public <T extends RepairablePrototype<?>> T getComponentBy(T source) {
        //noinspection unchecked
        var another = (T) componentPool.getNonBrokenComponentByClass(source.getClass());

        if (another != null) {
            log.info("Got component from pool: {}", another);
            return another;
        }

        var factory = configureFactory(source);
        log.trace("Creating component {} by component factory {}", source, factory);
        return factory.createComponentBy(source);
    }

    private <T extends RepairablePrototype<?>> SoulComponentFactory configureFactory(T o) {
        return o instanceof ArtificialSoulComponentMarker ? artificialSoulComponentFactory : humanSoulComponentFactory;
    }

}
