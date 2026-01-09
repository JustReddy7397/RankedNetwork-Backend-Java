package network.ranked.backend.socket.packets.map;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * @author JustReddy
 */
@Document(collection = "maps")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SlimeWorldMap {

    private String name;
    private GameType gameType;
    private String gridFsId;
    @Builder.Default
    private final Instant creationTime = Instant.now();
    private Instant lastModified;

}
