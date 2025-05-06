package ru.mai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mai.model.soul.ArtificialSoul;
import ru.mai.model.soul.HumanSoul;
import ru.mai.service.RepairingService;

@RestController
@RequestMapping("/fix")
@Tag(name = "Maintenance controller", description = "Controller for processing requests on repairing soul")
public class MaintenanceController {

    private static final Logger log = LoggerFactory.getLogger(MaintenanceController.class);
    @Autowired
    private RepairingService repairingService;

    @PostMapping("/human")
    @Operation(description = "Request for human soul curing")
    public void cureHumanSoul(@RequestBody HumanSoul soul) {
        log.info("Got human soul to cure: {}", soul);
        repairingService.putForRepairing(soul);
    }

    @PostMapping("/artificial")
    @Operation(description = "Request for artificial soul curing")
    public void cureArtificialSoul(@RequestBody ArtificialSoul soul) {
        log.info("Got artificial soul to cure: {}", soul);
        repairingService.putForRepairing(soul);
    }

}
