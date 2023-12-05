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
        double tarifaPorHora = tarifaService.obtenerTarifaPorHora();
        double tarifaPorMinuto = tarifaService.obtenerTarifaPorMinuto();
        model.addAttribute("tarifaPorHora", tarifaPorHora);
        model.addAttribute("tarifaPorMinuto", tarifaPorMinuto);
        return "confirmacionTarifa";
    } 

    // Otros métodos para actualizar tarifas, etc.
}
