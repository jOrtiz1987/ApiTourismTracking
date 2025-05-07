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
    @JoinColumn(name = "idLugarInteres")
    private LugarInteres lugarInteres;

    @ManyToOne
    @JoinColumn(name = "idPeriodoVacacional")
    private PeriodoVacacional periodoVacacional;

    public int getIdLugaresPorUsuario() {
        return idLugaresPorUsuario;
    }

    public void setIdLugaresPorUsuario(int idLugaresPorUsuario) {
        this.idLugaresPorUsuario = idLugaresPorUsuario;
    }

    public LugarInteres getLugarInteres() {
        return lugarInteres;
    }

    public void setLugarInteres(LugarInteres lugarInteres) {
        this.lugarInteres = lugarInteres;
    }

    public PeriodoVacacional getIdPeriodoVacacional() {
        return periodoVacacional;
    }

    public void setIdPeriodoVacacional(PeriodoVacacional periodoVacacional) {
        this.periodoVacacional = periodoVacacional;
    }
}
