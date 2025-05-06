package ru.mai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mai.model.RepairablePrototype;
import ru.mai.model.soul.Soul;
import ru.mai.service.RepairingService;
import ru.mai.service.WorkshopService;
import ru.mai.service.pool.ComponentPool;
import ru.mai.service.pool.WorkshopLog;

import java.util.List;

@RestController
@RequestMapping("/monitor")
@Tag(name = "Maintenance facade controller", description = "Controller for getting info on current system state")
public class MaintenanceFacadeController {

    @Autowired
    private ComponentPool componentPool;

    @Autowired
    private WorkshopLog workshopLog;

    @Autowired
    private WorkshopService workshopService;

    @Autowired
    private RepairingService repairingService;

    @GetMapping("/component-pool")
    @Operation(description = "Returns elements currently available in component pool")
    public List<RepairablePrototype> getComponentPoolElements(@RequestParam int page,
                                                              @RequestParam int count) {
        return componentPool.getByPageAndCount(page, count);
    }

    @GetMapping("/active-queries")
    @Operation(description = "Returns queries to be processed by repairing service")
    public List<Soul> getActiveQueries(@RequestParam int page,
                                       @RequestParam int count) {
        return repairingService.getActiveQueriesByPageAndCount(page, count);
    }

    @GetMapping("/workshop")
    @Operation(description = "Returns queries to be processed by workshop service")
    public List<RepairablePrototype> getWorkshopQueries(@RequestParam int page,
                                                        @RequestParam int count) {
        return workshopService.getActiveQueriesByPageAndCount(page, count);
    }

    @GetMapping("/workshop-log")
    @Operation(description = "Returns all components processed or in process by workshop service")
    public List<WorkshopLog.WorkshopLogEntity> getWorkshopLog(@RequestParam int page,
                                                              @RequestParam int count) {
        return workshopLog.getByPageAndSize(page, count);
    }

    @GetMapping("/repaired")
    @Operation(description = "Returns all repaired components processed by workshop service")
    public List<WorkshopLog.WorkshopLogEntity> getRepaired(@RequestParam int page,
                                                           @RequestParam int count) {
        return workshopLog.getRepairedByPageAndSize(page, count);
    }

    @GetMapping("/utilized")
    @Operation(description = "Returns all utilized components processed by workshop service")
    public List<WorkshopLog.WorkshopLogEntity> getUtilized(@RequestParam int page,
                                                           @RequestParam int count) {
        return workshopLog.getUtilizedByPageAndSize(page, count);
    }

}
