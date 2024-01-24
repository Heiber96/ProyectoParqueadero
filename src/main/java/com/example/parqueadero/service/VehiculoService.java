package com.example.parqueadero.service;

import org.springframework.stereotype.Service;
import com.example.parqueadero.models.Vehiculo;
import com.example.parqueadero.service.repository.VehiculoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public void guardarVehiculo(Vehiculo vehiculo) {
        vehiculoRepository.save(vehiculo);
    }

    public List<Vehiculo> obtenerTodosLosVehiculos() {
        return vehiculoRepository.findAll();
    }

    public Optional<Vehiculo> obtenerVehiculoPorId(Long id) {
        return vehiculoRepository.findById(id);
    }

    public void eliminarVehiculo(Long id) {
        vehiculoRepository.deleteById(id);
    }

    public List<Vehiculo> obtenerVehiculosNoSalidos() {
        return vehiculoRepository.findByHoraSalidaIsNull();
    }

    public List<Vehiculo> obtenerVehiculosSalidos() {
        return vehiculoRepository.findByHoraSalidaIsNotNull();
    }

    public List<Vehiculo> obtenerHistorialPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa);
    }

    public Vehiculo buscarVehiculoSalidoPorPlaca(String placa) {
        return vehiculoRepository.findByPlacaAndHoraSalidaIsNotNull(placa);
    }

    public Vehiculo buscarVehiculoConHoraSalidaPorPlaca(String placa) {
        return vehiculoRepository.findByPlacaAndHoraSalidaIsNotNull(placa);
    }

    public List<String> obtenerHistorialPlacasVehiculosSalidos() {
        return vehiculoRepository.obtenerHistorialPlacasVehiculosSalidos();
    }

    public void actualizarVehiculo(Vehiculo vehiculo) {
        if (vehiculo != null && vehiculo.getId() != null) {
            vehiculoRepository.save(vehiculo);
        } else {
            // Puedes manejar un caso de error o lanzar una excepción según tus necesidades
            throw new IllegalArgumentException("El vehículo o su ID no pueden ser nulos");
        }
    }
}

