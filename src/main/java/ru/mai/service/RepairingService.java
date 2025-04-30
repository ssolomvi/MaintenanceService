package ru.mai.service;

import org.apache.commons.lang3.BooleanUtils;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mai.fabric.ArtificialSoulComponentFactory;
import ru.mai.fabric.HumanSoulComponentFactory;
import ru.mai.fabric.SoulComponentFactory;
import ru.mai.model.RepairablePrototype;
import ru.mai.model.marker.ArtificialSoulComponentMarker;
import ru.mai.model.soul.Soul;
import ru.mai.service.pool.ComponentPool;

@Service
@SuppressWarnings("rawtypes")
// todo: add queue
public class RepairingService {

    private static final Logger log = LoggerFactory.getLogger(RepairingService.class);
    private final WorkshopService workshopService;
    private final ComponentPool componentPool;
    private final ArtificialSoulComponentFactory artificialSoulComponentFactory;
    private final HumanSoulComponentFactory humanSoulComponentFactory;

    @Autowired
    public RepairingService(WorkshopService workshopService,
                            ComponentPool componentPool) {
        this.workshopService = workshopService;
        this.componentPool = componentPool;
        this.artificialSoulComponentFactory = ArtificialSoulComponentFactory.getArtificialSoulComponentFactory();
        this.humanSoulComponentFactory = HumanSoulComponentFactory.getHumanSoulComponentFactory();
    }

    public void repairSoul(Soul soul) {
        Preconditions.checkNotNull(soul, "Soul is not passed for repairing");

        log.trace("Checking if consciousness needs curing...");
        soul.setConsciousness(sendForRepairAndAcquireAnotherIfNeeded(soul.getConsciousness()));
        log.trace("Checking if emotion chip needs curing...");
        soul.setEmotionChip(sendForRepairAndAcquireAnotherIfNeeded(soul.getEmotionChip()));
        log.trace("Checking if memory module needs curing...");
        soul.setMemoryModule(sendForRepairAndAcquireAnotherIfNeeded(soul.getMemoryModule()));
        log.info("Cured another soul! {}", soul);
    }

    private <T extends RepairablePrototype> T sendForRepairAndAcquireAnotherIfNeeded(T component) {
        if (isBroken(component)) {
            log.debug("Component {} is broken", component);
            if (component != null) {
                log.trace("Sending component {} to repair", component);
                sendForRepair(component);
            }

            log.trace("Acquiring another component alike {}", component);
            return acquireAnother(component);
        }

        log.debug("Component {} is not broken and does not need fixing", component);
        return component;
    }

    private void sendForRepair(RepairablePrototype component) {
        workshopService.putForRepair(component);
    }

    private <T extends RepairablePrototype> T acquireAnother(T component) {
        //noinspection unchecked
        var another = (T) componentPool.getNonBrokenComponentByClass(component.getClass());

        if (another != null) {
            log.debug("Got component from pool: {}", another);
            return another;
        }

        var factory = configureFactory(component);
        log.trace("Creating component {} by component factory {}", component, factory);
        return factory.createComponentBy(component);
    }

    private SoulComponentFactory configureFactory(Object o) {
        return o instanceof ArtificialSoulComponentMarker ? artificialSoulComponentFactory : humanSoulComponentFactory;
    }

    private boolean isBroken(RepairablePrototype component) {
        return component == null || BooleanUtils.toBooleanDefaultIfNull(component.getBroken(), true);
    }

}
