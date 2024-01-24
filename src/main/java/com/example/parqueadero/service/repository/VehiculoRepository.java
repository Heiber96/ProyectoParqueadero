package com.example.parqueadero.service.repository;

import com.example.parqueadero.models.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    List<Vehiculo> findByHoraSalidaIsNull();

    List<Vehiculo> findByHoraSalidaIsNotNull();
    
    List<Vehiculo> findByPlaca(String placa);

    Vehiculo findByPlacaAndHoraSalidaIsNotNull(String placa);

    // Nuevo método para obtener el historial de placas de vehículos salidos
    @Query("SELECT DISTINCT v.placa FROM Vehiculo v WHERE v.horaSalida IS NOT NULL")
    List<String> obtenerHistorialPlacasVehiculosSalidos();
}

