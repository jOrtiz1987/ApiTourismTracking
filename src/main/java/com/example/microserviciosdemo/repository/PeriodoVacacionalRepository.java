package com.example.microserviciosdemo.repository;

import com.example.microserviciosdemo.entity.PeriodoVacacional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodoVacacionalRepository extends JpaRepository<PeriodoVacacional, Integer> {
}
