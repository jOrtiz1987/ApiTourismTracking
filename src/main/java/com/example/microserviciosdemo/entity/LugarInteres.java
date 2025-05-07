package com.example.microserviciosdemo.entity;

import javax.persistence.*;

@Entity
@Table(name = "LugarInteres")
public class LugarInteres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLugarInteres")
    private int id;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "latitud")
    private double latitud;

    @Column(name = "longitud")
    private double longitud;

    @Column(name = "referenciaImagen")
    private String referenciaImagen;

    @Column(name = "contenido")
    private String contenido;

    @ManyToOne
    @JoinColumn(name = "idCategoria")
    private Categoria categoria;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getReferenciaImagen() {
        return referenciaImagen;
    }

    public void setReferenciaImagen(String referenciaImagen) {
        this.referenciaImagen = referenciaImagen;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
