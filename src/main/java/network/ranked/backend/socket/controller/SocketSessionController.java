package network.ranked.backend.socket.controller;

import io.rsocket.RSocket;
import network.ranked.backend.socket.store.SocketSessionStore;
import network.ranked.backend.util.Common;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;

/**
 * @author JustReddy
 */
@Controller
public class SocketSessionController {

    private final SocketSessionStore sessionStore;

    public SocketSessionController(SocketSessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    @ConnectMapping
    public void onConnect(RSocketRequester requester) {
        System.out.println("WHAAAAAAT?");
        sessionStore.createSession(requester.rsocketClient());

        final RSocket socket = requester.rsocket();


        if (socket == null) {
            return;
        }

        final String token = socket.availability() > 0 ? socket.toString() : "unknown";

        Common.logInfo(token + " connected");

        socket.onClose()
                .doFinally(signal -> sessionStore.removeSession(requester.rsocketClient()))
                .subscribe();
    }

}
