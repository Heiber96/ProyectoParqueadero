package com.example.parqueadero.service.impl;

import com.example.parqueadero.models.Vehiculo;

import java.util.List;

public interface VehiculoServiceImpl {
    List<Vehiculo> obtenerTodosLosVehiculos();

    Vehiculo obtenerVehiculoPorId(Long id);

    void guardarVehiculo(Vehiculo vehiculo);

    void actualizarVehiculo(Vehiculo vehiculo);

    void eliminarVehiculo(Long id);
}
