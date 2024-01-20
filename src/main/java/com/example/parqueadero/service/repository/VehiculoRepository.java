package com.example.parqueadero.service.repository;

import com.example.parqueadero.models.Vehiculo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    List<Vehiculo> findByHoraSalidaIsNull();
    List<Vehiculo> findByHoraSalidaIsNotNull();
    Vehiculo findByPlacaAndHoraSalidaIsNotNull(String placa);// Puedes agregar consultas personalizadas aquí si es necesario
}
