package com.example.parqueadero.service.repository;

import com.example.parqueadero.models.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
}
