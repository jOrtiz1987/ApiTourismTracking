package com.example.microserviciosdemo.repository;

import com.example.microserviciosdemo.entity.Visita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitaRepository extends JpaRepository<Visita, Integer> {

}
