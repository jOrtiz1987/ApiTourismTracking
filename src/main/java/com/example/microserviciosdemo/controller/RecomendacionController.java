package com.example.microserviciosdemo.controller;

import com.example.microserviciosdemo.entity.LugarInteres;
import com.example.microserviciosdemo.entity.Usuario;
import com.example.microserviciosdemo.repository.LugarInteresRepository;
import com.example.microserviciosdemo.repository.UsuarioRepository;
import com.example.microserviciosdemo.repository.VisitaRepository;
import com.example.microserviciosdemo.security.SecurityUtil;
import com.example.microserviciosdemo.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Recomendaciones para la app móvil (cualquier usuario con sesión).
 * El usuario se toma del token, no de la petición.
 */
@RestController
@RequestMapping("/api/recomendaciones")
public class RecomendacionController {

    private static final Logger log = LoggerFactory.getLogger(RecomendacionController.class);

    private final ReportService reportService;
    private final UsuarioRepository usuarioRepository;
    private final LugarInteresRepository lugarInteresRepository;
    private final VisitaRepository visitaRepository;

    public RecomendacionController(ReportService reportService, UsuarioRepository usuarioRepository,
                                   LugarInteresRepository lugarInteresRepository, VisitaRepository visitaRepository) {
        this.reportService = reportService;
        this.usuarioRepository = usuarioRepository;
        this.lugarInteresRepository = lugarInteresRepository;
        this.visitaRepository = visitaRepository;
    }

    /**
     * Siguiente lugar sugerido después de {idLugar}, según la red neuronal de data-info.
     * Descarta el lugar actual y los que el usuario ya visitó (si todos están visitados, usa el
     * más probable distinto al actual). Responde 204 si no hay sugerencia o el modelo no responde.
     */
    @GetMapping("/siguiente/{idLugar}")
    public ResponseEntity<Map<String, Object>> siguiente(@PathVariable int idLugar) {
        Usuario usuario = usuarioRepository.findByCorreo(SecurityUtil.correoActual())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        List<Integer> candidatos;
        try {
            Map<String, Object> peticion = new HashMap<>();
            peticion.put("userId", usuario.getIdUsuario());
            peticion.put("currentPoiId", idLugar);
            candidatos = candidatos(reportService.predictNextMonument(peticion));
        } catch (Exception e) {
            log.warn("No se pudo obtener la recomendación para el lugar {}: {}", idLugar, e.getMessage());
            return ResponseEntity.noContent().build();
        }

        Integer elegido = null;
        for (Integer id : candidatos) {
            if (id != idLugar && !visitaRepository.existsByUsuario_IdUsuarioAndLugarInteres_Id(usuario.getIdUsuario(), id)) {
                elegido = id;
                break;
            }
        }
        if (elegido == null) {
            elegido = candidatos.stream().filter(id -> id != idLugar).findFirst().orElse(null);
        }
        if (elegido == null) {
            return ResponseEntity.noContent().build();
        }

        Optional<LugarInteres> lugar = lugarInteresRepository.findById(elegido);
        if (!lugar.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("id", lugar.get().getId());
        respuesta.put("descripcion", lugar.get().getDescripcion());
        respuesta.put("referenciaImagen", lugar.get().getReferenciaImagen());
        respuesta.put("contenido", lugar.get().getContenido());
        return ResponseEntity.ok(respuesta);
    }

    /** Lugares recomendados en orden: el predicho y luego el top 3 por probabilidad. */
    @SuppressWarnings("unchecked")
    private static List<Integer> candidatos(Map<String, Object> prediccion) {
        Set<Integer> ids = new LinkedHashSet<>();
        if (prediccion == null) {
            return new ArrayList<>();
        }
        Object recomendado = prediccion.get("recommended_poi_id");
        if (recomendado instanceof Number && "success".equals(prediccion.get("status"))) {
            ids.add(((Number) recomendado).intValue());
        }
        Object top = prediccion.get("top_recommendations");
        if (top instanceof List) {
            for (Object o : (List<Object>) top) {
                if (o instanceof Map && ((Map<String, Object>) o).get("poi_id") instanceof Number) {
                    ids.add(((Number) ((Map<String, Object>) o).get("poi_id")).intValue());
                }
            }
        }
        return new ArrayList<>(ids);
    }
}
