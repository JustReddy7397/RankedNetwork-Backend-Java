package network.ranked.backend.socket;

/**
 * @author JustReddy
 */
public record Ack<T>(T data, String error) {

    public static <T> Ack<T> ok(T data) {
        return new Ack<>(data, null);
    }

    public static <T> Ack<T> error(String error) {
        return new Ack<>(null, error);
    }

}
