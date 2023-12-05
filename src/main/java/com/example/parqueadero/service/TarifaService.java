package com.example.parqueadero.service;


import org.springframework.stereotype.Service;

import com.example.parqueadero.service.TarifaServiceImpl.TarifaCalculadoraService;
import com.example.parqueadero.service.TarifaServiceImpl.TarifaPorHoraService;
import com.example.parqueadero.service.TarifaServiceImpl.TarifaPorMinutoService;

@Service
public class TarifaService implements TarifaPorHoraService, TarifaPorMinutoService, TarifaCalculadoraService {

    private static final double TARIFA_POR_HORA = 4500;
    private static final double TARIFA_POR_MINUTO = 75;
    private static final int MAX_MINUTOS_AL_DIA = 1440;

    @Override
    public double obtenerTarifaPorHora() {
        return TARIFA_POR_HORA;
    }

    @Override
    public double obtenerTarifaPorMinuto() {
        return TARIFA_POR_MINUTO;
    }

    @Override
    public double calcularTarifa(int minutos) {
        if (minutos < 0) {
            throw new IllegalArgumentException("El número de minutos no puede ser negativo");
        }

        int minutosLimitados = Math.min(minutos, MAX_MINUTOS_AL_DIA);
        return minutosLimitados * TARIFA_POR_MINUTO;
    }
}
 