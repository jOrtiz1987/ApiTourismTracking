package com.example.microserviciosdemo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PeriodoVacacional")
public class PeriodoVacacional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPeriodoVacacional")
    private int idPeriodoVacacional;

    @Column(name = "presupuesto")
    private Double presupuesto;

    @Column(name = "fechaInicioEstimada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicioEstimada;

    @Column(name = "fechaFinEstimada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinEstimada;

    @Column(name = "fechaInicioReal")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicioReal;

    @Column(name = "fechaFinReal")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinReal;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    public int getIdPeriodoVacacional() {
        return idPeriodoVacacional;
    }

    public void setIdPeriodoVacacional(int idPeriodoVacacional) {
        this.idPeriodoVacacional = idPeriodoVacacional;
    }

    public Double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Date getFechaInicioEstimada() {
        return fechaInicioEstimada;
    }

    public void setFechaInicioEstimada(Date fechaInicioEstimada) {
        this.fechaInicioEstimada = fechaInicioEstimada;
    }

    public Date getFechaFinEstimada() {
        return fechaFinEstimada;
    }

    public void setFechaFinEstimada(Date fechaFinEstimada) {
        this.fechaFinEstimada = fechaFinEstimada;
    }

    public Date getFechaInicioReal() {
        return fechaInicioReal;
    }

    public void setFechaInicioReal(Date fechaInicioReal) {
        this.fechaInicioReal = fechaInicioReal;
    }

    public Date getFechaFinReal() {
        return fechaFinReal;
    }

    public void setFechaFinReal(Date fechaFinReal) {
        this.fechaFinReal = fechaFinReal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
