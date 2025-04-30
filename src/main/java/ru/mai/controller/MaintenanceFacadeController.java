package ru.mai.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class MaintenanceFacadeController {

//    @Autowired
//    private ComponentPool componentPool;
//
//    @Autowired
//    private
//
//    // todo: controller, queues, logging
//    //  журнал того что отправлено на ремонт
//    //  что отремонтировано
//    //  что утилизировано
//    @GetMapping("/component-pool")
//    public List<RepairablePrototype> getComponentPoolElements(@RequestParam int page, @RequestParam int count) {
//        return componentPool.getByPageAndCount(page, count);
//    }
//
//    @GetMapping("/active-queries")
//    public List<RepairablePrototype> getActiveQueries() {
//
//    }
//
//    @GetMapping("/workshop")
//    public List<RepairablePrototype> getWorkshopElements() {
//
//    }
//
//    @GetMapping("/utilized")
//    public List<RepairablePrototype> getUtilized() {
//
//    }
//
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
