package network.ranked.backend.socket.services;

import lombok.RequiredArgsConstructor;
import network.ranked.backend.repositories.SlimeWorldMapRepository;
import network.ranked.backend.repositories.service.SlimeWorldMapStorageService;
import network.ranked.backend.socket.packets.map.GameType;
import network.ranked.backend.socket.packets.map.SlimeWorldMap;
import network.ranked.backend.util.Common;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * @author JustReddy
 */
@Service
@RequiredArgsConstructor
public class SlimeWorldMapService {

    private final SlimeWorldMapRepository repository;
    private final SlimeWorldMapStorageService storageService;

    public SlimeWorldMap createMap(String name, GameType type, byte[] buffer) {
        final String gridFsId = storageService.upload(name, buffer);

        final SlimeWorldMap map = SlimeWorldMap.builder()
                .name(name)
                .gameType(type)
                .gridFsId(gridFsId)
                .build();

        return repository.save(map);
    }

    public byte[] downloadWorld(String gridFsId) {
        try {
            return storageService.download(gridFsId);
        } catch (Exception e) {
            Common.logError("Failed to download world with GridFS ID: " + gridFsId);
            e.printStackTrace();
        }
        return null;
    }

    public Optional<SlimeWorldMap> getByNameAndType(String name, GameType type) {
        return repository.findByNameAndGameType(name, type);
    }

    public List<SlimeWorldMap> list(GameType type) {
        return type == null ? repository.findAll() : repository.findAllByGameType(type);
    }

    public void updateMeta(String name, GameType type, SlimeWorldMap update) {
        repository.findByNameAndGameType(name, type)
                .ifPresent(map -> {
                    map.setLastModified(Instant.now());
                    if (update.getGridFsId() != null) {
                        map.setGridFsId(update.getGridFsId());
                    }
                    if (update.getGameType() != null) {
                        map.setGameType(update.getGameType());
                    }
                    if (update.getName() != null) {
                        map.setName(update.getName());
                    }
                    repository.save(map);
                });
    }


}
