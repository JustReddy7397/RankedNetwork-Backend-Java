package network.ranked.backend.socket.packets.connection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author JustReddy
 */
public class PlayerConnectionUpdateResult {

    private final Status status;
    private String message;
    private Date expiryDate;

    public PlayerConnectionUpdateResult(Status status) {
        this.status = status;
    }

    @JsonCreator
    public PlayerConnectionUpdateResult(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public PlayerConnectionUpdateResult(Status status, Date expiryDate) {
        this.status = status;
        this.expiryDate = expiryDate;
    }

    @JsonCreator
    public PlayerConnectionUpdateResult(Status status, String message, Date expiryDate) {
        this.status = status;
        this.message = message;
        this.expiryDate = expiryDate;
    }

    public enum Status {
        SUCCESS,
        BANNED,
        ERROR
    }

}
