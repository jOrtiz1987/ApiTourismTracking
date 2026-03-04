package com.example.microserviciosdemo.entity;

import javax.persistence.*;

@Entity
@Table(name = "LugaresPorUsuario")
public class LugaresPorUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLugaresPorUsuario")
    private int idLugaresPorUsuario;

    @ManyToOne
    @JoinColumn(name = "idCategoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "idPeriodoVacacional")
    private PeriodoVacacional periodoVacacional;

    public int getIdLugaresPorUsuario() {
        return idLugaresPorUsuario;
    }

    public void setIdLugaresPorUsuario(int idLugaresPorUsuario) {
        this.idLugaresPorUsuario = idLugaresPorUsuario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public PeriodoVacacional getIdPeriodoVacacional() {
        return periodoVacacional;
    }

    public void setIdPeriodoVacacional(PeriodoVacacional periodoVacacional) {
        this.periodoVacacional = periodoVacacional;
    }
}
