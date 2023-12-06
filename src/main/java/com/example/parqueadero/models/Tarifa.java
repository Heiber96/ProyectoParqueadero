package com.example.parqueadero.models;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Component
@Entity
@Table
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double tarifaPorHora;
    private double tarifaPorMinuto;

    // Constructores, getters y setters

    public Long getId() {
        return id;
    }

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

