package com.example.microserviciosdemo.repository;

import com.example.microserviciosdemo.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

}
