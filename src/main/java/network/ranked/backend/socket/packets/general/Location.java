package network.ranked.backend.socket.packets.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author JustReddy
 */
@AllArgsConstructor
@Getter
public class Location {

    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    @JsonCreator
    public Location(String world, double x, double y, double z) {
        this(world, x, y, z, 0.0f, 0.0f);
    }

}
