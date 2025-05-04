package ru.mai.controller;

import jakarta.validation.constraints.Min;
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
public class MaintenanceFacadeController {

    @Autowired
    private ComponentPool componentPool;

    @Autowired
    private WorkshopLog workshopLog;

    @Autowired
    private WorkshopService workshopService;

    @Autowired
    private RepairingService repairingService;

    // todo: controller, queues, logging
    //  журнал того что отправлено на ремонт
    //  что отремонтировано
    //  что утилизировано
    // индексация page с 0
    @GetMapping("/component-pool")
    public List<RepairablePrototype> getComponentPoolElements(@Min(0) @RequestParam int page,
                                                              @Min(1) @RequestParam int count) {
        return componentPool.getByPageAndCount(page, count);
    }

    @GetMapping("/active-queries")
    public List<Soul> getActiveQueries(@Min(0) @RequestParam int page,
                                       @Min(1) @RequestParam int count) {
        return repairingService.getActiveQueriesByPageAndCount(page, count);
    }

    @GetMapping("/workshop")
    public List<RepairablePrototype> getWorkshopQueries(@Min(0) @RequestParam int page,
                                                        @Min(1) @RequestParam int count) {
        return workshopService.getActiveQueriesByPageAndCount(page, count);
    }

    @GetMapping("/workshop-log")
    public List<WorkshopLog.WorkshopLogEntity> getWorkshopLog(@Min(0) @RequestParam int page,
                                                              @Min(1) @RequestParam int count) {
        return workshopLog.getByPageAndSize(page, count);
    }

    @GetMapping("/repaired")
    public List<WorkshopLog.WorkshopLogEntity> getRepaired(@Min(0) @RequestParam int page,
                                                           @Min(1) @RequestParam int count) {
        return workshopLog.getRepairedByPageAndSize(page, count);
    }

    @GetMapping("/utilized")
    public List<WorkshopLog.WorkshopLogEntity> getUtilized(@Min(0) @RequestParam int page,
                                                           @Min(1) @RequestParam int count) {
        return workshopLog.getUtilizedByPageAndSize(page, count);
    }

    /*
    @GetMapping("/component-pool")
    public Page<RepairablePrototype> getComponentPoolElements(@ParameterObject Pageable pageable) {

    }

    @GetMapping("/active-queries")
    public Page<RepairablePrototype> getActiveQueries(@ParameterObject Pageable pageable) {

    }

    @GetMapping("/workshop")
    public Page<RepairablePrototype> getWorkshopElements(@ParameterObject Pageable pageable) {

    }

    @GetMapping("/utilized")
    public Page<RepairablePrototype> getUtilized(@ParameterObject Pageable pageable) {

    }
    */

}
