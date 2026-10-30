package com.example.microserviciosdemo.controller;

import com.example.microserviciosdemo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")

public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * Genera el reporte en Python y regresa metadata (reportId + summary + links)
     * Útil para UI (mostrar heatmap/clusters y permitir descarga).
     */
    @PostMapping("/generate/meta")
    public ResponseEntity<Map<String, Object>> generateReportMeta(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(reportService.generateReportMeta(request));
    }

    /**
     * Descarga el Excel generado por reportId.
     */
    @GetMapping("/{reportId}/excel")
    public ResponseEntity<byte[]> downloadExcel(@PathVariable String reportId) {
        return reportService.downloadExcel(reportId);
    }

    /**
     * Descarga/abre el heatmap HTML generado por reportId.
     * Se puede mostrar en un iframe o abrir en una pestaña.
     */
    @GetMapping("/{reportId}/heatmap")
    public ResponseEntity<byte[]> downloadHeatmap(@PathVariable String reportId) {
        return reportService.downloadHtml(reportId, "heatmap", "mapa_heatmap.html");
    }

    /**
     * Descarga/abre el mapa de clusters HTML generado por reportId.
     */
    @GetMapping("/{reportId}/clusters")
    public ResponseEntity<byte[]> downloadClusters(@PathVariable String reportId) {
        return reportService.downloadHtml(reportId, "clusters", "mapa_clusters_global.html");
    }

    /**
     * Entrena el recomendador de red neuronal en Python.
     */
    @PostMapping("/recommend/train")
    public ResponseEntity<Map<String, Object>> trainRecommendationModel() {
        return ResponseEntity.ok(reportService.trainRecommendationModel());
    }

    /**
     * Predice el siguiente monumento recomendado.
     */
    @PostMapping("/recommend/next")
    public ResponseEntity<Map<String, Object>> predictNextMonument(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(reportService.predictNextMonument(request));
    }

    /**
     * Entrena el clasificador de perfiles en Python.
     */
    @PostMapping("/recommend/profile/train")
    public ResponseEntity<Map<String, Object>> trainProfileModel() {
        return ResponseEntity.ok(reportService.trainProfileModel());
    }

    /**
     * Predice el perfil del turista.
     */
    @PostMapping("/recommend/profile")
    public ResponseEntity<Map<String, Object>> predictTouristProfile(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(reportService.predictTouristProfile(request));
    }

    /**
     * Entrena el estimador de presupuesto en Python.
     */
    @PostMapping("/recommend/budget/train")
    public ResponseEntity<Map<String, Object>> trainBudgetModel() {
        return ResponseEntity.ok(reportService.trainBudgetModel());
    }

    /**
     * Predice el presupuesto estimado del turista.
     */
    @PostMapping("/recommend/budget")
    public ResponseEntity<Map<String, Object>> predictTouristBudget(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(reportService.predictTouristBudget(request));
    }
}