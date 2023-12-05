package com.example.parqueadero.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.parqueadero.models.Vehiculo;

@Service
public class VehiculoService {
    private static final List<Vehiculo> vehiculos = new ArrayList<>();

    public void guardarVehiculo(Vehiculo vehiculo) {
        vehiculos.add(vehiculo);
    }

    public List<Vehiculo> obtenerTodosLosVehiculos() {
        return new ArrayList<>(vehiculos);
    }

    public void eliminarVehiculo(int id) {
        vehiculos.removeIf(vehiculo -> vehiculo.getId() == id);
    }
}
