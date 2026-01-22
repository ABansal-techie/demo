package com.example.demo.health;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> details = new HashMap<>();
        details.put("status", "UP");
        details.put("components", Map.of(
            "db", Map.of("status", "UP"),
            "diskSpace", Map.of("status", "UP")
        ));
        return ResponseEntity.ok(details);
    }
}
