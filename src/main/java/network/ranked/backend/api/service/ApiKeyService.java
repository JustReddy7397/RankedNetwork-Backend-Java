package network.ranked.backend.api.service;

import lombok.RequiredArgsConstructor;
import network.ranked.backend.redis.RedisApiKeyRepository;
import network.ranked.backend.redis.RedisPlayerRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author JustReddy
 */
@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final RedisApiKeyRepository apiKeyRepository;

    public String generateKey() {
        // TODO REPLACE WITH PROPER RANDOM API KEY GENERATION
        String key = UUID.randomUUID().toString();
        apiKeyRepository.storeKey(key);
        return key;
    }

    public boolean validateKey(String apiKey) {
        return apiKeyRepository.exists(apiKey);
    }

    public void revokeKey(String apiKey) {
        apiKeyRepository.delete(apiKey);
    }

}
