package com.example.microserviciosdemo.controller;

import com.example.microserviciosdemo.entity.PeriodoVacacional;
import com.example.microserviciosdemo.entity.Usuario;
import com.example.microserviciosdemo.repository.PeriodoVacacionalRepository;
import com.example.microserviciosdemo.repository.UsuarioRepository;
import com.example.microserviciosdemo.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/periodos")
public class PeriodoVacacionalController {

    private final PeriodoVacacionalRepository repository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public PeriodoVacacionalController(PeriodoVacacionalRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<PeriodoVacacional> getAllPeriodosVacacional() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public PeriodoVacacional getPeriodoVacacionalById(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Periodo Vacacional not found"));
    }

    @PostMapping
    public PeriodoVacacional createPeriodoVacacional(@RequestBody PeriodoVacacional periodoVacacional) {
        // Siempre crea uno nuevo y solo para el propio usuario (o cualquiera si es administrador)
        if (periodoVacacional.getUsuario() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el usuario");
        }
        Usuario usuario = usuarioRepository.findById(periodoVacacional.getUsuario().getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        SecurityUtil.exigirDuenoOAdmin(usuario.getCorreo());
        periodoVacacional.setIdPeriodoVacacional(0);
        periodoVacacional.setUsuario(usuario);
        return repository.save(periodoVacacional);
    }

    @PutMapping("/{id}")
    public PeriodoVacacional updatePeriodoVacacional(@PathVariable int id, @RequestBody PeriodoVacacional periodoVacacionalDetails) {
        PeriodoVacacional periodoVacacional = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Periodo Vacacional not found"));

        periodoVacacional.setUsuario(periodoVacacionalDetails.getUsuario());
        periodoVacacional.setFechaFinEstimada(periodoVacacionalDetails.getFechaFinEstimada());
        periodoVacacional.setFechaFinReal(periodoVacacionalDetails.getFechaFinReal());
        periodoVacacional.setPresupuesto(periodoVacacionalDetails.getPresupuesto());
        periodoVacacional.setFechaInicioEstimada(periodoVacacionalDetails.getFechaInicioEstimada());
        periodoVacacional.setFechaInicioReal(periodoVacacionalDetails.getFechaInicioReal());
        return repository.save(periodoVacacional);
    }

    @DeleteMapping("/{id}")
    public void deletePeriodoVacacional(@PathVariable int id) {
        repository.deleteById(id);
    }
}
