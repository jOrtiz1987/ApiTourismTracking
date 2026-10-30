package com.example.microserviciosdemo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ReportService {

    @Value("${python.api.url:http://localhost:8000}")
    private String pythonApiUrl;

    // Llave compartida con data-info (variable de entorno PYTHON_API_KEY); sin valor por defecto
    @Value("${python.api.key}")
    private String pythonApiKey;

    private final RestTemplate restTemplate;

    // Los reportId que genera data-info son los primeros 10 caracteres de un UUID
    private static final java.util.regex.Pattern REPORT_ID = java.util.regex.Pattern.compile("^[a-f0-9-]{1,36}$");

    public ReportService() {
        org.springframework.http.client.SimpleClientHttpRequestFactory factory =
                new org.springframework.http.client.SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);
        factory.setReadTimeout(300_000); // el entrenamiento de modelos puede tardar varios minutos
        this.restTemplate = new RestTemplate(factory);
    }

    private static void validarReportId(String reportId) {
        if (reportId == null || !REPORT_ID.matcher(reportId).matches()) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "reportId inválido");
        }
    }

    /**
     * Llama a Python para generar el reporte.
     * Regresa el JSON de Python:
     * - status
     * - summary (incluye reportId)
     * - downloads (excel/heatmap/clusters)
     */
    public Map<String, Object> generateReportMeta(Map<String, Object> requestBody) {

        String generateUrl = pythonApiUrl + "/reports/generate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", pythonApiKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> genResponse = restTemplate.exchange(
                generateUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        if (!genResponse.getStatusCode().is2xxSuccessful() || genResponse.getBody() == null) {
            throw new RuntimeException("Python report generation failed");
        }

        return genResponse.getBody();
    }

    /**
     * Descarga el Excel por reportId desde Python:
     * GET /reports/{reportId}/excel
     */
    public ResponseEntity<byte[]> downloadExcel(String reportId) {
        validarReportId(reportId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", pythonApiKey);

        String downloadUrl = pythonApiUrl + "/reports/" + reportId + "/excel";

        ResponseEntity<byte[]> fileResponse = restTemplate.exchange(
                downloadUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                byte[].class
        );

        if (!fileResponse.getStatusCode().is2xxSuccessful() || fileResponse.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                ))
                .body(fileResponse.getBody());
    }

    /**
     * Descarga HTML (heatmap o clusters) por reportId desde Python:
     * GET /reports/{reportId}/{type}
     * donde type = heatmap | clusters
     */
    public ResponseEntity<byte[]> downloadHtml(String reportId, String type, String filename) {
        validarReportId(reportId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", pythonApiKey);

        String downloadUrl = pythonApiUrl + "/reports/" + reportId + "/" + type;

        ResponseEntity<byte[]> fileResponse = restTemplate.exchange(
                downloadUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                byte[].class
        );

        if (!fileResponse.getStatusCode().is2xxSuccessful() || fileResponse.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }

        // inline => se puede renderizar en navegador/iframe
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
                .contentType(MediaType.TEXT_HTML)
                .body(fileResponse.getBody());
    }

    public Map<String, Object> trainRecommendationModel() {
        String url = pythonApiUrl + "/recommend/train";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", pythonApiKey);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(headers),
                Map.class
        );
        return response.getBody();
    }

    public Map<String, Object> predictNextMonument(Map<String, Object> requestBody) {
        String url = pythonApiUrl + "/recommend/next";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", pythonApiKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );
        return response.getBody();
    }

    public Map<String, Object> trainProfileModel() {
        String url = pythonApiUrl + "/recommend/profile/train";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", pythonApiKey);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(headers),
                Map.class
        );
        return response.getBody();
    }

    public Map<String, Object> predictTouristProfile(Map<String, Object> requestBody) {
        String url = pythonApiUrl + "/recommend/profile";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", pythonApiKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );
        return response.getBody();
    }

    public Map<String, Object> trainBudgetModel() {
        String url = pythonApiUrl + "/recommend/budget/train";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", pythonApiKey);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(headers),
                Map.class
        );
        return response.getBody();
    }

    public Map<String, Object> predictTouristBudget(Map<String, Object> requestBody) {
        String url = pythonApiUrl + "/recommend/budget";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", pythonApiKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );
        return response.getBody();
    }
}