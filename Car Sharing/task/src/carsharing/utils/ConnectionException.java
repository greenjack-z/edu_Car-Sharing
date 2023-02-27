package carsharing.utils;

public class ConnectionException extends RuntimeException {
    public ConnectionException(Exception e) {
        super(e);
    }
}
