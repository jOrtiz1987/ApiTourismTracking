package com.example.microserviciosdemo.controller;

import com.example.microserviciosdemo.entity.PeriodoVacacional;
import com.example.microserviciosdemo.repository.PeriodoVacacionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/periodos")
public class PeriodoVacacionalController {

    private final PeriodoVacacionalRepository repository;

    @Autowired
    public PeriodoVacacionalController(PeriodoVacacionalRepository repository) {
        this.repository = repository;
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
