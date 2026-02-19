package com.example.microserviciosdemo.controller;

import com.example.microserviciosdemo.entity.LugarInteres;
import com.example.microserviciosdemo.repository.LugarInteresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/api/edificios")
public class LugarInteresController {

    private final LugarInteresRepository repository;

    private static final double RADIUS_OF_EARTH_METERS = 6371000;

    @Autowired
    public LugarInteresController(LugarInteresRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<LugarInteres> getAllLugares() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public LugarInteres getLugarById(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lugar not found"));
    }

    @PostMapping
    public LugarInteres createLugar(@RequestBody LugarInteres lugarInteres) {
        return repository.save(lugarInteres);
    }

    public LugarInteres encontrarLugarInteresMasCercano(double latitud, double longitud) {
        List<LugarInteres> todosLosLugares = repository.findAll();
        LugarInteres lugarMasCercano = null;
        double distanciaMinima = Double.MAX_VALUE; // Inicializa la distancia mínima a un valor alto
        System.out.println("Coordenadas = lat - " + latitud + " - long - " + longitud);
        for (LugarInteres lugar : todosLosLugares) {
            double distancia = calcularDistancia(latitud, longitud, lugar.getLatitud(), lugar.getLongitud());
            System.out.println("Distancia = " + distancia);
            // Verifica si el lugar está dentro del rango de 50 metros
            if (distancia <= 50) {
                // Si encontramos una distancia menor, actualizamos el lugar más cercano
                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    lugarMasCercano = lugar;
                }
            }
        }

        return lugarMasCercano; // Retorna el lugar más cercano o null si no hay ninguno
    }

    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIUS_OF_EARTH_METERS * c; // Distancia en metros
    }

    @PutMapping("/{id}")
    public LugarInteres updateLugar(@PathVariable int id, @RequestBody LugarInteres lugarInteres) {
        LugarInteres lugar = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lugar Interes not found"));

        lugar.setDescripcion(lugarInteres.getDescripcion());
        lugar.setLatitud(lugarInteres.getLatitud());
        lugar.setLongitud(lugarInteres.getLongitud());
        lugar.setReferenciaImagen(lugarInteres.getReferenciaImagen());
        lugar.setContenido(lugarInteres.getContenido());

        return repository.save(lugar);
    }

    @DeleteMapping("/{id}")
    public void deleteLugar(@PathVariable int id) {
        repository.deleteById(id);
    }
}
