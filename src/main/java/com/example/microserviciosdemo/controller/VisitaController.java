package com.example.microserviciosdemo.controller;

import com.example.microserviciosdemo.entity.Visita;
import com.example.microserviciosdemo.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitas")
public class VisitaController {

    private final VisitaRepository repository;

    @Autowired
    public VisitaController(VisitaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Visita> getAllVistas() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Visita getVisitaById(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visita not found"));
    }

    @PostMapping
    public Visita createVisita(@RequestBody Visita visita) {
        return repository.save(visita);
    }

    @PutMapping("/{id}")
    public Visita updateVisita(@PathVariable int id, @RequestBody Visita visitaDetails) {
        Visita visita = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visita not found"));

        visita.setFecha(visitaDetails.getFecha());
        visita.setUsuario(visitaDetails.getUsuario());
        visita.setLugarInteres(visitaDetails.getLugarInteres());

        return repository.save(visita);
    }

    @DeleteMapping("/{id}")
    public void deleteVisita(@PathVariable int id) {
        repository.deleteById(id);
    }
}
