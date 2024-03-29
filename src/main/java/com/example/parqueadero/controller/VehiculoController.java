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

                    // Calcula la tarifa total
                    double tarifaTotal = minutosEstacionado * tarifaPorMinuto;

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
        // Guarda solo la placa y la hora de entrada en la base de datos
        Vehiculo vehiculoGuardado = new Vehiculo();
        vehiculoGuardado.setPlaca(vehiculo.getPlaca());
        vehiculoGuardado.setTipo(vehiculo.getTipo());  // Asegúrate de establecer el tipo correctamente
        vehiculoGuardado.setHoraEntrada(LocalDateTime.now());
        vehiculoService.guardarVehiculo(vehiculoGuardado);

        // Actualiza la tarifa según el tipo de vehículo guardado
        double tarifaPorMinuto = (vehiculoGuardado.getTipo().equalsIgnoreCase("carro"))
                ? tarifaService.obtenerTarifaPorMinutoCarro()
                : tarifaService.obtenerTarifaPorMinutoMoto();

        // Agrega la tarifa por minuto al modelo
        model.addAttribute("tarifaPorMinuto", tarifaPorMinuto);

        // Muestra la placa ingresada en la consola (puedes quitar esto después de verificar)
        System.out.println("Placa ingresada: " + vehiculoGuardado.getPlaca());

        // Agrega los atributos al modelo
        model.addAttribute("mensaje",
                "Vehículo con placa " + vehiculoGuardado.getPlaca() + " ha ingresado al parqueadero.");

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

                // Calcula el tiempo transcurrido en minutos utilizando el método de la clase
                // Vehiculo
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

    @GetMapping("/buscarPorPlaca")
public String buscarPorPlaca(@RequestParam String placa, Model model) {
    try {
        // Busca todos los registros de vehículos (entradas y salidas) asociados a la placa
        List<Vehiculo> vehiculos = vehiculoService.obtenerHistorialPorPlaca(placa);

        // Agrega los resultados al modelo
        model.addAttribute("vehiculosSalidos", vehiculos);

        return "listarVehiculosSalidos";
    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("error", "Ocurrió un error al buscar el historial por placa.");
        return "errorPage";
    }
}

   /*  @GetMapping("/buscarVehiculoSalido")
    public String buscarVehiculoSalidoPorPlaca(@RequestParam String placa, Model model) {
        Vehiculo vehiculo = vehiculoService.buscarVehiculoSalidoPorPlaca(placa);
        model.addAttribute("vehiculoEncontrado", vehiculo);
        return "mostrarVehiculoBuscado";
    }

     @GetMapping("/vehiculo-salido")
    //public String mostrarVehiculoBuscado(@RequestParam String placa, Model model) {
      //  try {
         //   Vehiculo vehiculoBuscado = vehiculoService.buscarVehiculoConHoraSalidaPorPlaca(placa);

         //   if (vehiculoBuscado != null) {
         //       model.addAttribute("vehiculo", vehiculoBuscado);
                return "mostrarVehiculoBuscado";
            } else {
                model.addAttribute("error", "Vehículo no encontrado");
                return "errorPage";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error al mostrar el vehículo buscado.");
            return "errorPage";
        }
        

    @GetMapping("/historial-vehiculos-salidos")
    public String mostrarHistorialVehiculosSalidos(Model model) {
        List<String> historialPlacas = vehiculoService.obtenerHistorialPlacasVehiculosSalidos();
        model.addAttribute("historialPlacas", historialPlacas);
        return "mostrarHistorialVehiculosSalidos";
    }*/

    // Resto del código...
}
