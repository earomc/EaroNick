package net.earomc.earonick.mojangapi;

public class MojangAPIException extends Exception {
    public MojangAPIException() {
    }

    public MojangAPIException(String message) {
        super(message);
    }

    public MojangAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public MojangAPIException(Throwable cause) {
        super(cause);
    }
}
