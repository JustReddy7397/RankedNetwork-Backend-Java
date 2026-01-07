package network.ranked.backend.socket.packets.party;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import network.ranked.backend.socket.packets.player.profile.PlayerProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JustReddy
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Party {

    private PlayerProfile leader;
    private List<PlayerProfile> members = new ArrayList<>();
    private List<PartyInvite> invites = new ArrayList<>();
    private boolean isPrivate = false;

}
