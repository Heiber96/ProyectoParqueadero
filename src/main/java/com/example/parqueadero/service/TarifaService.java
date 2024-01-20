// TarifaService.java

package com.example.parqueadero.service;

import org.springframework.stereotype.Service;

@Service
public class TarifaService {

    private static final double TARIFA_POR_MINUTO_CARRO = 90.0;
    private static final double TARIFA_POR_MINUTO_MOTO = 75.0;

    public double obtenerTarifaPorMinutoCarro() {
        System.out.println("Obteniendo tarifa por minuto para carro: " + TARIFA_POR_MINUTO_CARRO);
        return TARIFA_POR_MINUTO_CARRO;
    }

    public double obtenerTarifaPorMinutoMoto() {
        System.out.println("Obteniendo tarifa por minuto para moto: " + TARIFA_POR_MINUTO_MOTO);
        return TARIFA_POR_MINUTO_MOTO;
    }
}
 