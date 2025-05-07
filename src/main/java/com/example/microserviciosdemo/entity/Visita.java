package com.example.microserviciosdemo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Visita")
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVisita")
    private int idVisita;

    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(name = "llevaNinos")
    private Boolean llevaNinos;

    @ManyToOne
    @JoinColumn(name = "idEdificioHistorico")
    private LugarInteres lugarInteres;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    public int getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(int idVisita) {
        this.idVisita = idVisita;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public LugarInteres getLugarInteres() {
        return lugarInteres;
    }

    public void setLugarInteres(LugarInteres lugarInteres) {
        this.lugarInteres = lugarInteres;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getLlevaNinos() {
        return llevaNinos;
    }

    public void setLlevaNinos(Boolean llevaNinos) {
        this.llevaNinos = llevaNinos;
    }
}
