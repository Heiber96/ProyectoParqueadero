package com.example.parqueadero.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.parqueadero.models.Tarifa;
import com.example.parqueadero.models.Vehiculo;
import com.example.parqueadero.service.VehiculoService;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private Tarifa tarifa;

    private int calcularMinutosEstacionado(LocalDateTime horaEntrada, LocalDateTime horaSalida) {
        if (horaEntrada != null && horaSalida != null) {
            return (int) ChronoUnit.MINUTES.between(horaEntrada, horaSalida);
        } else {
            return 0;
        }
    }

    @PostMapping("/salida")
public String procesarSalida(@ModelAttribute Vehiculo vehiculo, Model model) {
    try {
        LocalDateTime horaEntrada = vehiculo.getHoraEntrada();
        LocalDateTime horaSalida = LocalDateTime.now(); // Nueva asignación para horaSalida

        System.out.println("Hora de Entrada: " + horaEntrada);
        System.out.println("Hora de Salida: " + horaSalida);
        // Calcula el tiempo transcurrido en minutos
        int minutosEstacionado = calcularMinutosEstacionado(horaEntrada, horaSalida);

        // Obtiene las tarifas desde el objeto de Tarifa
        double tarifaPorHora = tarifa.getTarifaPorHora();
        double tarifaPorMinuto = tarifa.getTarifaPorMinuto();

        // Calcula la tarifa total
        double tarifaTotal = (minutosEstacionado / 60.0 * tarifaPorHora) + (minutosEstacionado % 60 * tarifaPorMinuto);

        // Asigna la hora de salida al vehículo después de calcular minutosEstacionado
        vehiculo.setHoraSalida(horaSalida);

        // Guarda el vehículo en la base de datos
        vehiculoService.guardarVehiculo(vehiculo);

        // Agrega los atributos al modelo
        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("horaSalida", horaSalida);
        model.addAttribute("horaEntrada", horaEntrada);
        model.addAttribute("minutosEstacionado", minutosEstacionado);
        model.addAttribute("tarifaPorMinuto", tarifaPorMinuto);
        model.addAttribute("tarifaPorHora", tarifaPorHora);
        model.addAttribute("tarifaTotal", tarifaTotal);

        return "confirmacionSalida";
    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("error", "Ocurrió un error al procesar la salida.");
        return "errorPage";
    }
}

    

    @GetMapping("/listado")
    public String mostrarListado(Model model) {
        model.addAttribute("vehiculos", vehiculoService.obtenerTodosLosVehiculos());
        return "listarVehiculos";
    }

    @GetMapping("/")
    public String mostrarFormulario(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        return "index";
    }

    @PostMapping("/")
    public String procesarEntrada(@ModelAttribute Vehiculo vehiculo, Model model) {
        vehiculoService.guardarVehiculo(vehiculo);
        model.addAttribute("mensaje", "Vehículo con placa " + vehiculo.getPlaca() + " ha ingresado al parqueadero.");
        return "index";
    }

    @GetMapping("/salida")
    public String mostrarFormularioSalida(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        return "confirmacionSalida";
    }
}