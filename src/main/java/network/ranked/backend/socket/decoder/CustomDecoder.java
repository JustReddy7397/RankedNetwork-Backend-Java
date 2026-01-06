package network.ranked.backend.socket.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

/**
 * @author JustReddy
 */
@Component
public class CustomDecoder {

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T decode(String payload, Class<T> type) {
        try {
            if (payload == null || payload.isEmpty()) {
                throw new IllegalArgumentException("Payload cannot be null or empty");
            }

            return mapper.readValue(payload, type);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid JSON payload", exception);
        }
    }

    public String encode(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Could not encode object to JSON", exception);
        }
    }

}
