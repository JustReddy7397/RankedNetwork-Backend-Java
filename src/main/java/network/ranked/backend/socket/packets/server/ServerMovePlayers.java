package network.ranked.backend.socket.packets.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import network.ranked.backend.socket.packets.identity.ClientPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JustReddy
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ServerMovePlayers {

    private List<ClientPlayer> players = new ArrayList<>();
    private String serverIdentifier;

}
