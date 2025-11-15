package edu.espe.springpruebaoscarchanataxi.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class VersionController {
    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String version;

    @Value("${app.build-number}")
    private String buildNumber;

    @Value("${app.commit-hash}")
    private String commitHash;

    /**
     * Endpoint que retorna información de la versión actual de la aplicación
     * Útil para verificar qué versión está desplegada en producción
     * @return Map con datos de versión, build y commit
     */
    @GetMapping("/version")
    public Map<String, String> getVersion() {
        Map<String, String> versionInfo = new HashMap<>();
        versionInfo.put("appName", appName);
        versionInfo.put("version", version);
        versionInfo.put("buildNumber", buildNumber);
        versionInfo.put("commitHash", commitHash);
        versionInfo.put("timestamp", Instant.now().toString());
        return versionInfo;
    }
}
