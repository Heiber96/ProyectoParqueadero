package com.example.parqueadero.service;

import org.springframework.stereotype.Service;
import com.example.parqueadero.models.Vehiculo;
import com.example.parqueadero.service.repository.VehiculoRepository;
import java.util.List;

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

    public void eliminarVehiculo(Long id) {
        vehiculoRepository.deleteById(id);
    }
}
