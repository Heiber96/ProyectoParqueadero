package com.example.parqueadero.models;

import java.time.LocalDateTime;

public class Vehiculo {
    private static int contadorIds = 1;

    private int id;
    private String tipo;
    private String placa;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    private int tiempoRestante;

    public Vehiculo() {
        this.id = contadorIds++;
        this.horaEntrada = LocalDateTime.now();
    }

    // Otros constructores, getters y setters

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }
}


