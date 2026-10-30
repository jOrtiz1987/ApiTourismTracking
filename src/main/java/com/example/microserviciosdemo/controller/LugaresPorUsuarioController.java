package com.example.microserviciosdemo.controller;

import com.example.microserviciosdemo.entity.Categoria;
import com.example.microserviciosdemo.entity.LugaresPorUsuario;
import com.example.microserviciosdemo.entity.PeriodoVacacional;
import com.example.microserviciosdemo.repository.CategoriaRepository;
import com.example.microserviciosdemo.repository.LugaresPorUsuarioRepository;
import com.example.microserviciosdemo.repository.PeriodoVacacionalRepository;
import com.example.microserviciosdemo.security.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lugares-usuario")
public class LugaresPorUsuarioController {

    private final LugaresPorUsuarioRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final PeriodoVacacionalRepository periodoVacacionalRepository;

    @Autowired
    public LugaresPorUsuarioController(LugaresPorUsuarioRepository repository,
            CategoriaRepository categoriaRepository,
            PeriodoVacacionalRepository periodoVacacionalRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.periodoVacacionalRepository = periodoVacacionalRepository;
    }

    @GetMapping
    public List<LugaresPorUsuario> getAllLugaresPorUsuario() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public LugaresPorUsuario getLugaresPorUsuarioById(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("LugaresPorUsuario not found"));
    }

    @PostMapping
    public LugaresPorUsuario createLugaresPorUsuario(@RequestBody LugaresPorUsuario request) {
        LugaresPorUsuario nuevoLugar = new LugaresPorUsuario();

        if (request.getCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoria().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
            nuevoLugar.setCategoria(categoria);
        }

        if (request.getIdPeriodoVacacional() != null) {
            PeriodoVacacional periodoVacacional = periodoVacacionalRepository
                    .findById(request.getIdPeriodoVacacional().getIdPeriodoVacacional())
                    .orElseThrow(() -> new IllegalArgumentException("PeriodoVacacional no encontrado"));
            // Solo se pueden agregar preferencias a un periodo propio (o como administrador)
            SecurityUtil.exigirDuenoOAdmin(periodoVacacional.getUsuario() != null ? periodoVacacional.getUsuario().getCorreo() : null);
            nuevoLugar.setIdPeriodoVacacional(periodoVacacional);
        } else if (!SecurityUtil.esAdmin()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el periodo vacacional");
        }

        return repository.save(nuevoLugar);
    }

    @PutMapping("/{id}")
    public LugaresPorUsuario updateLugaresPorUsuario(@PathVariable int id,
            @RequestBody LugaresPorUsuario requestDetails) {
        LugaresPorUsuario lugar = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("LugaresPorUsuario not found"));

        lugar.setCategoria(requestDetails.getCategoria());
        lugar.setIdPeriodoVacacional(requestDetails.getIdPeriodoVacacional());

        return repository.save(lugar);
    }

    @DeleteMapping("/{id}")
    public void deleteLugaresPorUsuario(@PathVariable int id) {
        repository.deleteById(id);
    }
}
