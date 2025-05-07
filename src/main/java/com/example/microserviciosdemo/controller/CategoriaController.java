package com.example.microserviciosdemo.controller;

import com.example.microserviciosdemo.entity.Categoria;
import com.example.microserviciosdemo.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaRepository repository;

    @Autowired
    public CategoriaController(CategoriaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Categoria> getAllCategorias() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Categoria getCategoriaById(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria not found"));
    }

    @PostMapping
    public Categoria createCategoria(@RequestBody Categoria categoria) {
        System.out.println("Voy a crear el categoria");
        return repository.save(categoria);
    }

    @PutMapping("/{id}")
    public Categoria updateCategoria(@PathVariable int id, @RequestBody Categoria categoriaDetails) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria not found"));

        categoria.setDescripcion(categoriaDetails.getDescripcion());

        return repository.save(categoria);
    }

    @DeleteMapping("/{id}")
    public void deleteCategoria(@PathVariable int id) {
        repository.deleteById(id);
    }
}
