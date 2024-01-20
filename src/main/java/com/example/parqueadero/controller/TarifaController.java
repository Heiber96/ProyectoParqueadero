package com.example.parqueadero.controller;

import com.example.parqueadero.service.TarifaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TarifaController {

    @Autowired
    private TarifaService tarifaService;

    @GetMapping("/tarifas")
    public String mostrarTarifas(Model model) {
        double tarifaPorMinutoCarro = tarifaService.obtenerTarifaPorMinutoCarro();
        double tarifaPorMinutoMoto = tarifaService.obtenerTarifaPorMinutoMoto();

        model.addAttribute("tarifaPorMinutoCarro", tarifaPorMinutoCarro);
        model.addAttribute("tarifaPorMinutoMoto", tarifaPorMinutoMoto);

        return "confirmacionTarifa";
    } 

    // Otros métodos para actualizar tarifas, etc.
}
