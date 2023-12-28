package com.example.parqueadero.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeFormatter;
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

    @RequestMapping(value = "/salida", method = { RequestMethod.GET, RequestMethod.POST })
    public String procesarSalida(@ModelAttribute Vehiculo vehiculo, Model model) {
        try {
            Optional<Vehiculo> optionalVehiculo = vehiculoService.obtenerVehiculoPorId(vehiculo.getId());

            if (optionalVehiculo.isPresent()) {
                Vehiculo vehiculoPersistente = optionalVehiculo.get();
                vehiculoPersistente.marcarHoraSalida();
                vehiculoService.guardarVehiculo(vehiculoPersistente);

                // Formatea la hora de salida
                String horaSalidaFormateada = vehiculoPersistente.getHoraSalida() != null
                        ? vehiculoPersistente.getHoraSalida().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        : "";

                // Calcula el tiempo transcurrido en minutos
                LocalDateTime horaEntrada = vehiculoPersistente.getHoraEntrada();
                LocalDateTime horaSalida = vehiculoPersistente.getHoraSalida();
                int minutosEstacionado = calcularMinutosEstacionado(horaEntrada, horaSalida);

                // Obtiene las tarifas desde el objeto de Tarifa
                double tarifaPorHora = tarifa.getTarifaPorHora();
                double tarifaPorMinuto = tarifa.getTarifaPorMinuto();

                // Calcula la tarifa total
                double tarifaTotal = (minutosEstacionado / 60.0 * tarifaPorHora)
                        + (minutosEstacionado % 60 * tarifaPorMinuto);

                // Agrega los atributos al modelo
                model.addAttribute("horaSalidaFormateada", horaSalidaFormateada);
                model.addAttribute("horaEntrada", vehiculoPersistente.getHoraEntrada() != null ? vehiculoPersistente.getHoraEntrada() : "");
                model.addAttribute("horaSalidaFormateada", horaSalidaFormateada);
                model.addAttribute("minutosEstacionado", minutosEstacionado >= 0 ? minutosEstacionado : "");
                model.addAttribute("tarifaPorMinuto", tarifaPorMinuto >= 0 ? tarifaPorMinuto : "");
                model.addAttribute("tarifaTotal", tarifaTotal >= 0 ? tarifaTotal : "");

                return "confirmacionSalida";
            } else {
                model.addAttribute("error", "Vehículo no encontrado en la base de datos.");
                return "errorPage";
            }
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
        try {
            // Establece la hora de entrada antes de guardar el vehículo
            vehiculo.setHoraEntrada(LocalDateTime.now());

            // Guarda el vehículo en la base de datos
            vehiculoService.guardarVehiculo(vehiculo);

            // Muestra la placa ingresada en la consola (puedes quitar esto después de
            // verificar)
            System.out.println("Placa ingresada: " + vehiculo.getPlaca());

            // Agrega los atributos al modelo
            model.addAttribute("mensaje",
                    "Vehículo con placa " + vehiculo.getPlaca() + " ha ingresado al parqueadero.");

            return "index";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error al procesar la entrada.");
            return "errorPage";
        }
    }

    @GetMapping("/salida/{id}")
    public String mostrarFormularioSalida(@PathVariable Long id, Model model) {
        // Obtén el vehículo desde el Optional
        Optional<Vehiculo> optionalVehiculo = vehiculoService.obtenerVehiculoPorId(id);

        if (optionalVehiculo.isPresent()) {
            // Si el vehículo está presente, obtén el objeto Vehiculo
            Vehiculo vehiculo = optionalVehiculo.get();

            // Agrega el vehículo al modelo
            model.addAttribute("vehiculo", vehiculo);

            return "confirmacionSalida";
        } else {
            // Maneja el caso en que no se encuentra el vehículo
            model.addAttribute("error", "Vehículo no encontrado");
            return "errorPage";
        }
    }
}
