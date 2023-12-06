package com.example.parqueadero.service.repository;

import com.example.parqueadero.models.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    // Puedes agregar consultas personalizadas aquí si es necesario
}
