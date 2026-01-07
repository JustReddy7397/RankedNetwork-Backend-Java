package network.ranked.backend.tasks;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import network.ranked.backend.redis.RedisPartyInviteRepository;
import network.ranked.backend.socket.packets.party.PartyInvite;
import network.ranked.backend.socket.parser.CustomParser;
import network.ranked.backend.socket.store.SocketSessionStore;
import network.ranked.backend.util.Common;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

/**
 * @author JustReddy
 */
@Component
@RequiredArgsConstructor
public class PartyInviteExpireTask {

    private final RedisPartyInviteRepository inviteRepository;
    private final SocketSessionStore sessionStore;
    private final CustomParser parser;

    @PostConstruct
    public void cleanOnStartup() {
        for (final PartyInvite invite : inviteRepository.getPartyInvites()) {
            inviteRepository.deletePartyInvite(invite.getInviter().getUniqueId(), invite.getInvited().getUniqueId());
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void expireInvites() {
        final long now = Instant.now().toEpochMilli();

        for (final PartyInvite invite : inviteRepository.getPartyInvites()) {
            if (invite.getExpiresAt().after(new Date(now))) {
                continue;
            }

            try {
                inviteRepository.deletePartyInvite(invite.getInviter().getUniqueId(), invite.getInvited().getUniqueId());

                sessionStore.broadcast("party:invite:timeout", parser.encode(invite));
            } catch (Exception exception) {
                Common.logWarning("Failed to expire party invite from " +
                        invite.getInviter().getCachedName() + " to " +
                        invite.getInvited().getCachedName() + ": " + exception.getMessage());
            }

        }
    }
}
