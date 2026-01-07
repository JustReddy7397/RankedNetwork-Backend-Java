package network.ranked.backend.socket.packets.party;

import lombok.AllArgsConstructor;
import lombok.Getter;
import network.ranked.backend.socket.packets.player.profile.PlayerProfile;

import java.time.Instant;
import java.util.Date;

/**
 * @author JustReddy
 */
@AllArgsConstructor
@Getter
public class PartyInvite {

    private PlayerProfile inviter;
    private PlayerProfile invited;
    private final Date expiresAt = Date.from(Instant.now().plusSeconds(60));

}
