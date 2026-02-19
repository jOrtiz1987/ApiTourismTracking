package com.example.microserviciosdemo.entity;

import java.util.Date;

public class VisitaRequest {

    private Integer idUsuario;
    private Integer idEdificioHistorico;
    private Boolean llevaNinos;
    private Date fecha;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdEdificioHistorico() {
        return idEdificioHistorico;
    }

    public void setIdEdificioHistorico(Integer idEdificioHistorico) {
        this.idEdificioHistorico = idEdificioHistorico;
    }

    public Boolean getLlevaNinos() {
        return llevaNinos;
    }

    public void setLlevaNinos(Boolean llevaNinos) {
        this.llevaNinos = llevaNinos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
