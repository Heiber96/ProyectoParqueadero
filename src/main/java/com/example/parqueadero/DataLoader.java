package com.example.parqueadero;

import com.example.parqueadero.models.Tarifa;
import com.example.parqueadero.service.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private TarifaRepository tarifaRepository;

    @Override
    public void run(String... args) {
        // Cargar datos iniciales de tarifas si es necesario
        if (tarifaRepository.count() == 0) {
            cargarDatosIniciales();
        }
    }

    private void cargarDatosIniciales() {
        // Crear y guardar una tarifa inicial en la base de datos
        Tarifa tarifaInicial = new Tarifa();
        tarifaInicial.setTarifaPorHora(4500);
        tarifaInicial.setTarifaPorMinuto(75);

        tarifaRepository.save(tarifaInicial);
    }
}
