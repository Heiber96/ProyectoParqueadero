package com.example.parqueadero.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;

    @Column
    private String placa;

    @Column(name = "hora_entrada")
    private LocalDateTime horaEntrada;

    @Column(name = "hora_salida")
    private LocalDateTime horaSalida;

    @Column(name = "tiempo_restante")
    private int tiempoRestante;

    @Column(name = "salido")
    private Boolean salido;

    public Vehiculo() {
        // No es necesario asignar un ID aquí, ya que la base de datos lo generará automáticamente
        this.horaEntrada = LocalDateTime.now();
        this.salido = false; // Por defecto, un vehículo no ha salido
    }

    public void marcarHoraSalida() {
        this.horaSalida = LocalDateTime.now();
        this.salido = true;
    }

    public String getHoraSalidaFormateada() {
        return horaSalida != null
                ? horaSalida.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : "";
    }

    public long calcularMinutosEstacionado() {
        if (horaEntrada != null && horaSalida != null) {
            return ChronoUnit.MINUTES.between(horaEntrada, horaSalida);
        } else {
            return 0;
        }
    }

    public boolean isSalido() {
        return salido;
    }

    public void setSalido(boolean salido) {
        this.salido = salido;
    }

    // Otros constructores, getters y setters...

    public Long getId() {
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
