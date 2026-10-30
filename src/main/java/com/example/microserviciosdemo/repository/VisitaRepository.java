package com.example.microserviciosdemo.repository;

import com.example.microserviciosdemo.entity.Visita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitaRepository extends JpaRepository<Visita, Integer> {

    /** true si el usuario ya tiene registrada una visita a ese lugar. */
    boolean existsByUsuario_IdUsuarioAndLugarInteres_Id(int idUsuario, int idLugarInteres);
}
