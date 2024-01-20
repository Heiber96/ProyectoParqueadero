package com.example.parqueadero.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

import com.example.parqueadero.models.Vehiculo;
import com.example.parqueadero.service.TarifaService;
import com.example.parqueadero.service.VehiculoService;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private TarifaService tarifaService;

    private int calcularMinutosEstacionado(LocalDateTime horaEntrada, LocalDateTime horaSalida) {
        return (horaEntrada != null && horaSalida != null)
                ? (int) ChronoUnit.MINUTES.between(horaEntrada, horaSalida)
                : 0;
    }

    @RequestMapping(value = "/salida", method = { RequestMethod.GET, RequestMethod.POST })
    public String procesarSalida(@ModelAttribute Vehiculo vehiculo, Model model) {
        try {
            Optional<Vehiculo> optionalVehiculo = vehiculoService.obtenerVehiculoPorId(vehiculo.getId());

            if (optionalVehiculo.isPresent()) {
                Vehiculo vehiculoPersistente = optionalVehiculo.get();

                if (vehiculoPersistente.getHoraSalida() == null) {
                    vehiculoPersistente.marcarHoraSalida();
                    vehiculoService.guardarVehiculo(vehiculoPersistente);

                    // Recarga el vehículo actualizado desde la base de datos
                    vehiculoPersistente = vehiculoService.obtenerVehiculoPorId(vehiculo.getId()).orElse(null);

                    // Calcula el tiempo transcurrido en minutos
                    long minutosEstacionado = calcularMinutosEstacionado(vehiculoPersistente.getHoraEntrada(),
                            vehiculoPersistente.getHoraSalida());

                    double tarifaPorMinuto = ("carro".equals(vehiculoPersistente.getTipo()))
                            ? tarifaService.obtenerTarifaPorMinutoCarro()
                            : tarifaService.obtenerTarifaPorMinutoMoto();

                    // Verifica el tipo de vehículo y asigna las tarifas correspondientes
                    double tarifaPorHora;
                    if ("carro".equals(vehiculoPersistente.getTipo())) {
                        tarifaPorHora = 90.0; // Tarifa para carros
                    } else if ("moto".equals(vehiculoPersistente.getTipo())) {
                        tarifaPorHora = 75.0; // Tarifa para motos
                    } else {
                        // Tipo de vehículo no reconocido, manejar de acuerdo a tus requisitos
                        tarifaPorHora = 1400.0; // Otra tarifa por defecto o manejo de error
                    }

                    // Calcula la tarifa total
                    double tarifaTotal = (minutosEstacionado / 60.0 * tarifaPorHora)
                            + (minutosEstacionado % 60 * tarifaPorMinuto);

                    // Agrega los atributos al modelo
                    model.addAttribute("vehiculo", vehiculoPersistente);
                    model.addAttribute("horaSalidaFormateada", vehiculoPersistente.getHoraSalida() != null
                            ? vehiculoPersistente.getHoraSalida()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            : "");
                    model.addAttribute("horaEntrada", vehiculoPersistente.getHoraEntrada() != null
                            ? vehiculoPersistente.getHoraEntrada()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            : "");
                    model.addAttribute("minutosEstacionado", minutosEstacionado);
                    model.addAttribute("tarifaPorMinuto", tarifaPorMinuto);
                    model.addAttribute("tarifaPorHora", tarifaPorHora);
                    model.addAttribute("tarifaTotal", tarifaTotal);

                    return "confirmacionSalida";
                } else {
                    model.addAttribute("error", "Este vehículo ya ha salido anteriormente.");
                    return "errorPage";
                }
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
        model.addAttribute("vehiculos", vehiculoService.obtenerVehiculosNoSalidos());
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

            // Muestra la placa ingresada en la consola (puedes quitar esto después de verificar)
            System.out.println("Placa ingresada: " + vehiculo.getPlaca());

            // Agrega los atributos al modelo
            model.addAttribute("mensaje",
                    "Vehículo con placa " + vehiculo.getPlaca() + " ha ingresado al parqueadero.");

            // Actualiza la tarifa según el tipo de vehículo
            double tarifaPorMinuto = (vehiculo.getTipo().equalsIgnoreCase("carro"))
                    ? tarifaService.obtenerTarifaPorMinutoCarro()
                    : tarifaService.obtenerTarifaPorMinutoMoto();

            // Agrega la tarifa por minuto al modelo
            model.addAttribute("tarifaPorMinuto", tarifaPorMinuto);

            return "index";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error al procesar la entrada.");
            return "errorPage";
        }
    }

    @RequestMapping(value = "/salida/{id}", method = RequestMethod.GET)
    public String mostrarFormularioSalida(@PathVariable Long id, Model model) {
        try {
            // Obtén el vehículo desde el Optional
            Optional<Vehiculo> optionalVehiculo = vehiculoService.obtenerVehiculoPorId(id);

            if (optionalVehiculo.isPresent()) {
                // Si el vehículo está presente, obtén el objeto Vehiculo
                Vehiculo vehiculo = optionalVehiculo.get();

                // Verifica si ya tiene hora de salida
                if (vehiculo.getHoraSalida() == null) {
                    // Marca la hora de salida
                    vehiculo.marcarHoraSalida();

                    // Actualiza los datos en la base de datos
                    vehiculoService.actualizarVehiculo(vehiculo);
                }

                System.out.println("Tipo de Vehículo: " + vehiculo.getTipo());

                // Obtiene la tarifa desde el servicio de Tarifa según el tipo de vehículo
                double tarifaPorMinuto = ("carro".equalsIgnoreCase(vehiculo.getTipo()))
                        ? tarifaService.obtenerTarifaPorMinutoCarro()
                        : tarifaService.obtenerTarifaPorMinutoMoto();

                // Calcula el tiempo transcurrido en minutos utilizando el método de la clase Vehiculo
                long minutosEstacionado = vehiculo.calcularMinutosEstacionado();

                // Calcula la tarifa total
                double tarifaTotal = minutosEstacionado * tarifaPorMinuto;

                // Agrega el vehículo al modelo
                model.addAttribute("vehiculo", vehiculo);
                model.addAttribute("minutosEstacionado", minutosEstacionado);
                model.addAttribute("tarifaPorMinuto", tarifaPorMinuto);
                model.addAttribute("tarifaTotal", tarifaTotal);

                return "confirmacionSalida";
            } else {
                // Maneja el caso en que no se encuentra el vehículo
                model.addAttribute("error", "Vehículo no encontrado");
                return "errorPage";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error al mostrar el formulario de salida.");
            return "errorPage";
        }
    }

    @GetMapping("/vehiculos-salidos")
    public String mostrarVehiculosSalidos(Model model) {
        List<Vehiculo> vehiculosSalidos = vehiculoService.obtenerVehiculosSalidos();
        model.addAttribute("vehiculosSalidos", vehiculosSalidos);
        return "listarVehiculosSalidos";
    }

    @GetMapping("/buscarVehiculoSalido")
    public String buscarVehiculoSalidoPorPlaca(@RequestParam String placa, Model model) {
        Vehiculo vehiculo = vehiculoService.buscarVehiculoSalidoPorPlaca(placa);
        model.addAttribute("vehiculoEncontrado", vehiculo);
        return "resultadoBusquedaVehiculo";
    }

    @GetMapping("/vehiculo-salido")
    public String mostrarVehiculoSalidoPorPlaca(@RequestParam String placa, Model model) {
        Vehiculo vehiculoSalido = vehiculoService.buscarVehiculoConHoraSalidaPorPlaca(placa);
        model.addAttribute("vehiculo", vehiculoSalido);
        return "mostrarVehiculoSalido"; // Asegúrate de tener la vista correspondiente
    }
    
    // Resto del código...
}
