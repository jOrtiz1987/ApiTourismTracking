package com.example.microserviciosdemo.controller;

import com.example.microserviciosdemo.entity.LugarInteres;
import com.example.microserviciosdemo.entity.Usuario;
import com.example.microserviciosdemo.entity.Visita;
import com.example.microserviciosdemo.entity.VisitaRequest;
import com.example.microserviciosdemo.repository.LugarInteresRepository;
import com.example.microserviciosdemo.repository.UsuarioRepository;
import com.example.microserviciosdemo.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitas")
public class VisitaController {

    private final VisitaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final LugarInteresRepository lugarInteresRepository;

    @Autowired
    public VisitaController(VisitaRepository repository, UsuarioRepository usuarioRepository, LugarInteresRepository lugarInteresRepository) {
        this.repository = repository;
        this.lugarInteresRepository = lugarInteresRepository;
        this.usuarioRepository = usuarioRepository;
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
    public Visita createVisita(@RequestBody VisitaRequest visita) {
        Visita nuevaVisita = new Visita();
        LugarInteres lugarInteres = lugarInteresRepository.findById(visita.getIdEdificioHistorico())
                .orElseThrow(() -> new IllegalArgumentException("LugarInteres no encontrado"));
        Usuario usuario = usuarioRepository.findById(visita.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        nuevaVisita.setLugarInteres(lugarInteres);
        nuevaVisita.setUsuario(usuario);
        nuevaVisita.setFecha(visita.getFecha());
        nuevaVisita.setLlevaNinos(visita.getLlevaNinos());

        return repository.save(nuevaVisita);
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
