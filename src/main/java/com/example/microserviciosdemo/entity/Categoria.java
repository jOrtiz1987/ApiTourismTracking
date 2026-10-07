package com.example.microserviciosdemo.entity;

import javax.persistence.*;

@Entity
@Table(name = "Categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCategoria")
    private int id;

    @Column(name = "descripcion")
    private String descripcion;

    public Categoria(){

    }

    public Categoria(int id, String descripcion){
        this.id = id;
        this.descripcion = descripcion;
    }

    public Categoria(String descripcion){
        this.descripcion = descripcion;
    }

    public Categoria(String descripcion, int id){
        this.id = id;
        this.descripcion = descripcion;
    }

    public Categoria(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
