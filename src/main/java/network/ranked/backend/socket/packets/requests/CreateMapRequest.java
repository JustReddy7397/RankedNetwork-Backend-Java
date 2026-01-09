package network.ranked.backend.socket.packets.requests;

import lombok.Data;
import network.ranked.backend.socket.packets.map.GameType;

/**
 * @author JustReddy
 */
@Data
public class CreateMapRequest {

    private String name;
    private GameType gameType;
    private byte[] buffer;

}
