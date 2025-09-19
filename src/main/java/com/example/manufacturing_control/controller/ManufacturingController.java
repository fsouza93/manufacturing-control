package com.example.manufacturing_control.controller;

import com.example.manufacturing_control.model.ProductionLine;
import com.example.manufacturing_control.model.ProductionResult;
import com.example.manufacturing_control.service.ManufacturingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ManufacturingController {

    @Autowired
    private ManufacturingService manufacturingService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/setup")
    public String setupProduction(@RequestParam("numberOfProducts") int numberOfProducts,
                                  @RequestParam("numberOfAssemblyStages") int numberOfAssemblyStages,
                                  Model model) {

        // Nesta versão permitimos 1 ou 2 montagens
        if (numberOfAssemblyStages < 1) numberOfAssemblyStages = 1;
        if (numberOfAssemblyStages > 2) numberOfAssemblyStages = 2;

        ProductionLine productionLine = new ProductionLine();
        productionLine.setNumberOfProducts(numberOfProducts);
        productionLine.setNumberOfAssemblyStages(numberOfAssemblyStages);
        productionLine.initializeProducts();

        model.addAttribute("productionLine", productionLine);
        return "time-input";
    }

    @PostMapping("/calculate")
    public String calculateProduction(@ModelAttribute ProductionLine productionLine, Model model) {
        ProductionResult result = manufacturingService.calculateProductionSchedule(productionLine);
        model.addAttribute("result", result);
        model.addAttribute("scale", 4); // (opcional) 1 min = 4px p/ gráfico futuro
        return "result";
    }
}
