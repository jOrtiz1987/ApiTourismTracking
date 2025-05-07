package com.example.microserviciosdemo.controller;

import com.example.microserviciosdemo.entity.LugarInteres;
import com.example.microserviciosdemo.entity.RegistroCoordendas;
import com.example.microserviciosdemo.repository.LugarInteresRepository;
import com.example.microserviciosdemo.repository.RegistroCoordendasRepository;
import com.example.microserviciosdemo.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/coordenadas")
public class RegistroCoordendasController {

    private final RegistroCoordendasRepository repository;
    private final LugarInteresController controllerLugarInteres;
    private final UsuarioRepository usuarioController;

    @Autowired
    public RegistroCoordendasController(RegistroCoordendasRepository repository, LugarInteresController controllerLugarInteres, UsuarioRepository usuarioController) {
        this.repository = repository;
        this.controllerLugarInteres = controllerLugarInteres;
        this.usuarioController = usuarioController;
    }

    @GetMapping
    public List<RegistroCoordendas> getAllCoordenadas() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public RegistroCoordendas getRegistroCoordendasById(@PathVariable int id) {
        System.out.println("Entra a registro coordenadas");
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RegistroCoordendas not found"));
    }

    @GetMapping("/validarCoordenadas/{userId}/{lat}/{lon}")
    public ResponseEntity<LugarInteres> validarCoordenadas(@PathVariable("userId") int idUsuario, @PathVariable("lat") double latitude, @PathVariable("lon") double longitud) {
        System.out.println("Entra a Validar Coordenadas");
        // Crear un objeto de RegistroCoordendas
        RegistroCoordendas coordenadas = new RegistroCoordendas();
        coordenadas.setLatitud(latitude);
        coordenadas.setLongitud(longitud);
        coordenadas.setFecha(new Date()); // Establecer la fecha actual
        coordenadas.setUsuario(usuarioController.getById(idUsuario)); // Establecer el ID del usuario correspondiente

        // Registrar las coordenadas
        repository.save(coordenadas);

        // Verificar si hay un Lugar de Interés cercano
        LugarInteres lugarCercano = controllerLugarInteres.encontrarLugarInteresMasCercano(latitude, longitud);

        if (lugarCercano != null) {
            System.out.println("Si encontre lugar de interes");
            return ResponseEntity.ok(lugarCercano); // Retorna el lugar de interés encontrado
        } else {
            System.out.println("No encontre lugar de interes");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // No se encontró ningún lugar de interés
        }
    }


    @PostMapping
    public RegistroCoordendas createRegistroCoordendas(@RequestBody RegistroCoordendas coordendas) {
        return repository.save(coordendas);
    }

    @PutMapping("/{id}")
    public RegistroCoordendas updateRegistroCoordendas(@PathVariable int id, @RequestBody RegistroCoordendas coordendasDetails) {
        RegistroCoordendas coordendas = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RegistroCoordendas not found"));

        coordendas.setLatitud(coordendasDetails.getLatitud());
        coordendas.setLongitud(coordendasDetails.getLongitud());
        coordendas.setFecha(coordendasDetails.getFecha());
        coordendas.setUsuario(coordendasDetails.getUsuario());

        return repository.save(coordendas);
    }

    @DeleteMapping("/{id}")
    public void deleteRegistroCoordendas(@PathVariable int id) {
        repository.deleteById(id);
    }
}
