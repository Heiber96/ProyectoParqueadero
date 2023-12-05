package com.example.parqueadero.models;
import org.springframework.stereotype.Component;

@Component
public class Tarifa {

    private double tarifaPorHora;
    private double tarifaPorMinuto;

    // Constructores, getters y setters

    public double getTarifaPorHora() {
        return tarifaPorHora;
    }

    public void setTarifaPorHora(double tarifaPorHora) {
        this.tarifaPorHora = tarifaPorHora;
    }

    public double getTarifaPorMinuto() {
        return tarifaPorMinuto;
    }

    public void setTarifaPorMinuto(double tarifaPorMinuto) {
        this.tarifaPorMinuto = tarifaPorMinuto;
    }
}


