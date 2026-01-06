package network.ranked.backend.repositories.custom.impl;

import lombok.RequiredArgsConstructor;
import network.ranked.backend.repositories.custom.ProfileRepositoryCustom;
import network.ranked.backend.socket.packets.player.PlayerProfile;
import network.ranked.backend.util.Common;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author JustReddy
 */
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void updateProfile(UUID uniqueId, Map<String, Object> fields) {
        Update update = new Update();
        fields.forEach(update::set);

        mongoTemplate.updateFirst(Query.query(Criteria.where("uniqueId").is(uniqueId)), update, PlayerProfile.class);
        Common.logInfo("MONGO -> Updated profile for " + uniqueId);
    }

    @Override
    public void updateProfiles(List<PlayerProfile> profiles, boolean isQuit) {

        for (final PlayerProfile profile : profiles) {

            final Update update = new Update();

            mongoTemplate.getConverter()
                    .write(profile, update.getUpdateObject());

            update.unset("_id");

            update.set("lastNetworkQuit",
                    isQuit ? Instant.now() : 0); // TODO

            final Query query = Query.query(
                    Criteria.where("uniqueId").is(profile.getUniqueId())
            );

            mongoTemplate.updateFirst(query, update, PlayerProfile.class);
            Common.logInfo("MONGO -> Updated profile for " + profile.getCachedName() + " (" + profile.getUniqueId() + ")");
        }

    }

    @Override
    public void updatePartialProfiles(List<UUID> uniqueIds, Map<String, Object> fields) {
        Update update = new Update();
        fields.forEach(update::set);

        mongoTemplate.updateFirst(Query.query(Criteria.where("uniqueId").in(uniqueIds)), update, PlayerProfile.class);
        Common.logInfo("MONGO -> Updated partial profiles for " + uniqueIds.size() + " players.");
    }
}
