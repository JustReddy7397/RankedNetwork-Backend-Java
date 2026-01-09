package network.ranked.backend.repositories;

import network.ranked.backend.socket.packets.map.GameType;
import network.ranked.backend.socket.packets.map.SlimeWorldMap;

import java.util.List;
import java.util.Optional;

/**
 * @author JustReddy
 */
public interface SlimeWorldMapRepository extends Repository<SlimeWorldMap> {

    Optional<SlimeWorldMap> findByNameAndGameType(String name, GameType type);

    List<SlimeWorldMap> findAllByGameType(GameType type);
}
