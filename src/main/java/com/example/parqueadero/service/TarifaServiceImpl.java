package com.example.parqueadero.service;

public interface TarifaServiceImpl {
    interface TarifaPorHoraService {
        double obtenerTarifaPorHora();
    }
    
    interface TarifaPorMinutoService {
        double obtenerTarifaPorMinuto();
    }
    
    interface TarifaCalculadoraService {
        double calcularTarifa(int minutos);
    }
}

