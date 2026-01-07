package network.ranked.backend.socket.packets.player.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import network.ranked.backend.socket.packets.player.enums.PlayerStatus;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;

/**
 * @author JustReddy
 */

@AllArgsConstructor
@Getter
public class DefaultPlayerProfile {

    @Indexed(unique = true)
    private UUID uniqueId;
    @Indexed()
    private String cachedName;
    private String discordId;
    private PlayerStatus status;

}
