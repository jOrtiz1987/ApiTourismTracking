package com.example.microserviciosdemo.repository;

import com.example.microserviciosdemo.entity.LugaresPorUsuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LugaresPorUsuarioRepository extends JpaRepository<LugaresPorUsuario, Integer> {

}
