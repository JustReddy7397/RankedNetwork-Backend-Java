package network.ranked.backend.api.controller;

import lombok.RequiredArgsConstructor;
import network.ranked.backend.api.service.ApiKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author JustReddy
 */
@RestController
@RequestMapping("/api/key")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping("/request")
    public ResponseEntity<ApiKeyResponse> requestApiKey() {
        String apiKey = apiKeyService.generateKey();
        return ResponseEntity.ok(new ApiKeyResponse(apiKey));
    }

    @GetMapping("/validate/{key}")
    public ResponseEntity<Void> validateApiKey(@PathVariable String key) {
        boolean valid = apiKeyService.validateKey(key);
        return valid ? ResponseEntity.ok().build() : ResponseEntity.status(401).build();
    }

    public record ApiKeyResponse(String key) {}
}
